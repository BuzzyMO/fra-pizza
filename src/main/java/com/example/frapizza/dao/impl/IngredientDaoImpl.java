package com.example.frapizza.dao.impl;

import com.example.frapizza.dao.IngredientDao;
import com.example.frapizza.entity.Ingredient;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IngredientDaoImpl implements IngredientDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(IngredientDaoImpl.class.getName());
  private final PgPool pool;

  public IngredientDaoImpl(PgPool pool) {
    this.pool = pool;
  }

  @Override
  public void save(JsonObject ingredientJson, Handler<AsyncResult<Void>> resultHandler) {
    Ingredient ingredient = ingredientJson.mapTo(Ingredient.class);
    String insertQuery = "INSERT INTO ingredients(name, cost) " +
      "VALUES ($1, $2) RETURNING id";
    pool.withTransaction(client -> client
        .preparedQuery(insertQuery)
        .execute(Tuple.of(ingredient.getName(), ingredient.getCost()))
        .map(rs -> rs.iterator().next().getInteger("id")))
      .onSuccess(id -> {
        LOGGER.info("Transaction succeeded: ingredient is created id:" + id);
        resultHandler.handle(Future.succeededFuture());
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: ingredient not created " + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
  }

  @Override
  public void update(Integer id, JsonObject ingredientJson, Handler<AsyncResult<Void>> resultHandler) {
    Ingredient ingredient = ingredientJson.mapTo(Ingredient.class);
    String updateQuery = "UPDATE ingredients SET(name, cost) = ($1, $2) " +
      "WHERE id=$3";
    pool.withTransaction(client -> client
        .preparedQuery(updateQuery)
        .execute(Tuple.of(ingredient.getName(), ingredient.getCost(), id)))
      .onSuccess(rs -> {
        LOGGER.warn("Transaction succeeded: ingredient is updated " + id);
        resultHandler.handle(Future.succeededFuture());
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: ingredient not updated " + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
  }

  @Override
  public void delete(Integer id, Handler<AsyncResult<Void>> resultHandler) {
    String deleteQuery = "DELETE FROM ingredients WHERE id=$1";
    pool.withTransaction(client -> client
        .preparedQuery(deleteQuery)
        .execute(Tuple.of(id)))
      .onSuccess(rs -> {
        LOGGER.warn("Transaction succeeded: ingredient is deleted " + id);
        resultHandler.handle(Future.succeededFuture());
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: ingredient not deleted " + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
  }

  @Override
  public void readAll(Handler<AsyncResult<JsonArray>> resultHandler) {
    String selectAllQuery = "SELECT* FROM ingredients";
    pool.withTransaction(client -> client
        .preparedQuery(selectAllQuery)
        .execute())
      .onSuccess(rs -> {
        JsonArray result = new JsonArray();
        for (Row row : rs) {
          result.add(row.toJson());
        }
        LOGGER.info("Transaction succeeded: ingredients is read");
        resultHandler.handle(Future.succeededFuture(result));
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: ingredients not read " + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
  }
}
