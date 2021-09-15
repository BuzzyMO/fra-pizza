package com.example.frapizza.dao.impl;

import com.example.frapizza.dao.PizzaDao;
import com.example.frapizza.entity.Pizza;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PizzaDaoImpl implements PizzaDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(PizzaDaoImpl.class.getName());
  private final PgPool pool;

  public PizzaDaoImpl(PgPool pool) {
    this.pool = pool;
  }

  @Override
  public void save(Pizza pizza, List<Integer> ingredientIds, Handler<AsyncResult<Void>> resultHandler) {
    String insertPizzaQuery = "INSERT INTO pizzas(name, description, created_by) " +
      "VALUES ($1, $2, $3) RETURNING id";
    pool.withTransaction(client -> client
        .preparedQuery(insertPizzaQuery)
        .execute(Tuple.of(pizza.getName(), pizza.getDescription(), pizza.getCreatedBy()))
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
  public void update(Long id, Pizza pizza, Handler<AsyncResult<Void>> resultHandler) {
    String updateQuery = "UPDATE pizzas SET(name, description, created_by, created_at) " +
      "= ($1, $2, $3, $4) WHERE id=$5";
    pool.withTransaction(client -> client
        .preparedQuery(updateQuery)
        .execute(Tuple.of(pizza.getName(), pizza.getDescription(), pizza.getCreatedBy(), pizza.getCreatedAt(), id)))
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
    String readQuery = "SELECT pizzas.id, pizzas.name as name, description, sum(i.cost) as cost " +
      "FROM pizzas " +
      "INNER JOIN users u on u.id = pizzas.created_by " +
      "INNER JOIN user_authorities ua on u.id = ua.user_id " +
      "INNER JOIN pizza_ingredients pi on pizzas.id = pi.pizza_id " +
      "INNER JOIN ingredients i on i.id = pi.ingredient_id " +
      "WHERE ua.authority_id = $1 " +
      "GROUP BY pizzas.id";
    pool.withTransaction(client -> client
        .preparedQuery(readQuery)
        .execute(Tuple.of(authorityId)))
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
  public void readByUser(Long userId, Handler<AsyncResult<JsonArray>> resultHandler) {
    String readQuery = "SELECT pizzas.id, pizzas.name as name, description, sum(i.cost) as cost " +
      "FROM pizzas " +
      "INNER JOIN pizza_ingredients pi on pizzas.id = pi.pizza_id " +
      "INNER JOIN ingredients i on i.id = pi.ingredient_id " +
      "WHERE created_by=$1 " +
      "GROUP BY pizzas.id";
    pool.withTransaction(client -> client
        .preparedQuery(readQuery)
        .execute(Tuple.of(userId)))
      .onSuccess(rs -> {
        JsonArray jsonArray = new JsonArray();
        for (Row row : rs) {
          jsonArray.add(row.toJson());
        }
        LOGGER.info("Transaction succeeded: pizzas is read by user " + userId);
        resultHandler.handle(Future.succeededFuture(jsonArray));
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: pizzas not read by user: " + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
  }

  @Override
  public void readAll(Handler<AsyncResult<JsonArray>> resultHandler) {
    String readAllQuery = "SELECT* FROM pizzas";
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
