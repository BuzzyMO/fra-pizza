package com.example.frapizza.dao.impl;

import com.example.frapizza.dao.PizzaDao;
import com.example.frapizza.entity.Pizza;
import com.example.frapizza.entity.Pizzeria;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PizzaDaoImpl implements PizzaDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(PizzaDaoImpl.class.getName());
  private final PgPool pool;

  public PizzaDaoImpl(PgPool pool) {
    this.pool = pool;
  }

  @Override
  public void save(JsonObject completePizza, Handler<AsyncResult<Void>> resultHandler) {
    Pizza pizza = completePizza.getJsonObject("pizza").mapTo(Pizza.class);
    List<Integer> ingredientIds = completePizza.getJsonArray("ingredients")
      .stream()
      .map(e -> (Integer) e)
      .collect(Collectors.toList());

    String insertPizzaQuery = "INSERT INTO pizzas(name, created_by) " +
      "VALUES ($1, $2) RETURNING id";
    pool.withTransaction(client -> client
        .preparedQuery(insertPizzaQuery)
        .execute(Tuple.of(pizza.getName(), pizza.getCreatedBy()))
        .map(rs -> rs.iterator().next().getInteger("id"))
        .compose(ar -> savePizzaIngredients(client, ar, ingredientIds))
        .map(rs -> rs.iterator().next().getInteger("pizza_id")))
      .onSuccess(id -> {
        LOGGER.info("Transaction succeeded: pizza is created " + id);
        resultHandler.handle(Future.succeededFuture());
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: pizza not created " + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
  }

  @Override
  public void update(JsonObject pizzaJson, Handler<AsyncResult<Void>> resultHandler) {
    Pizza pizza = pizzaJson.mapTo(Pizza.class);
    String updateQuery = "UPDATE pizzas SET(name, created_by, created_at) " +
      "= ($1, $2, $3) WHERE id=$4";
    pool.withTransaction(client -> client
        .preparedQuery(updateQuery)
        .execute(Tuple.of(pizza.getName(), pizza.getCreatedBy(), pizza.getCreatedAt(), pizza.getId())))
      .onSuccess(rs -> {
        LOGGER.warn("Transaction succeeded: pizza is updated " + pizza.getId());
        resultHandler.handle(Future.succeededFuture());
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: pizza not updated " + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
  }

  @Override
  public void delete(Long id, Handler<AsyncResult<Void>> resultHandler) {
    String deleteQuery = "DELETE FROM pizzas WHERE id=$1";
    pool.withTransaction(client -> client
        .preparedQuery(deleteQuery)
        .execute(Tuple.of(id)))
      .onSuccess(rs -> {
        LOGGER.warn("Transaction succeeded: pizza is deleted " + id);
        resultHandler.handle(Future.succeededFuture());
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: pizza not deleted " + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
  }

  @Override
  public void readByAuthority(Integer authorityId, Handler<AsyncResult<JsonArray>> resultHandler) {
    String readQuery = "SELECT pizzas.id, name FROM pizzas " +
      "INNER JOIN users u on u.id = pizzas.created_by " +
      "INNER JOIN user_authorities ua on u.id = ua.user_id WHERE ua.authority_id=$1";
    pool.withTransaction(client -> client
        .preparedQuery(readQuery)
        .execute())
      .onSuccess(rs -> {
        JsonArray jsonArray = new JsonArray();
        for (Row row : rs) {
          jsonArray.add(row.toJson());
        }
        LOGGER.info("Transaction succeeded: pizzas is read by authority");
        resultHandler.handle(Future.succeededFuture(jsonArray));
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: pizzas not read by authority: " + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
  }

  @Override
  public void readAll(Handler<AsyncResult<JsonArray>> resultHandler) {
    String readAllQuery = "SELECT* FROM pizzas INNER JOIN pizza_ingredients pi on pizzas.id = pi.pizza_id INNER JOIN ingredients i on i.id = pi.ingredient_id";
    pool.withTransaction(client -> client
        .preparedQuery(readAllQuery)
        .execute())
      .onSuccess(rs -> {
        JsonArray jsonArray = new JsonArray();
        for (Row row : rs) {
          jsonArray.add(row.toJson());
        }
        LOGGER.info("Transaction succeeded: pizzas is read");
        resultHandler.handle(Future.succeededFuture(jsonArray));
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: pizzas not read: " + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
  }

  private Future<RowSet<Row>> savePizzaIngredients(SqlConnection client, Integer pizzaId, List<Integer> ingredientIds) {
    List<Tuple> batch = new ArrayList<>();
    for (Integer i : ingredientIds) {
      batch.add(Tuple.of(pizzaId, i));
    }

    String insertPizzaIngredientsQuery = "INSERT INTO pizza_ingredients(pizza_id, ingredient_id) " +
      "VALUES ($1, $2) RETURNING pizza_id";
    return client
      .preparedQuery(insertPizzaIngredientsQuery)
      .executeBatch(batch);
  }


}
