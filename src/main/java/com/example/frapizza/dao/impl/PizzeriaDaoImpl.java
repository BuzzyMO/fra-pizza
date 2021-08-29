package com.example.frapizza.dao.impl;

import com.example.frapizza.dao.PizzeriaDao;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PizzeriaDaoImpl implements PizzeriaDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(PizzeriaDaoImpl.class.getName());
  private final PgPool pool;

  public PizzeriaDaoImpl(PgPool pool) {
    this.pool = pool;
  }

  @Override
  public void save(JsonObject pizzeriaJson, Handler<AsyncResult<Void>> resultHandler) {

  }

  @Override
  public void readAll(Handler<AsyncResult<JsonArray>> resultHandler) {
    String readAllQuery = "SELECT* FROM pizzerias";
    pool.withTransaction(client -> client
        .preparedQuery(readAllQuery)
        .execute())
      .onSuccess(rs -> {
        JsonArray jsonArray = new JsonArray();
        for (Row row : rs) {
          jsonArray.add(row.toJson());
        }
        LOGGER.info("Transaction succeeded: pizzerias is read");
        resultHandler.handle(Future.succeededFuture(jsonArray));
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: pizzerias not read: " + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
  }
}
