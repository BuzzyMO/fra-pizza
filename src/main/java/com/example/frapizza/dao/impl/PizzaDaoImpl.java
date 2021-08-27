package com.example.frapizza.dao.impl;

import com.example.frapizza.dao.PizzaDao;
import com.example.frapizza.entity.Pizza;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
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
        .compose(ar -> savePizzaIngredients(client, ar, ingredientIds)))
      .onSuccess(rs -> {
        LOGGER.info("Transaction succeeded: pizza is created");
        resultHandler.handle(Future.succeededFuture());
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: pizza not created" + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
  }

  private Future<RowSet<Row>> savePizzaIngredients(SqlConnection client, Integer pizzaId, List<Integer> ingredientIds) {
    List<Tuple> batch = new ArrayList<>();
    for (Integer i : ingredientIds) {
      batch.add(Tuple.of(pizzaId, i));
    }

    String insertPizzaIngredientsQuery = "INSERT INTO pizza_ingredients(pizza_id, ingredient_id) " +
      "VALUES ($1, $2)";
    return client
      .preparedQuery(insertPizzaIngredientsQuery)
      .executeBatch(batch);
  }
}
