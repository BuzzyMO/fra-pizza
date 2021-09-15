package com.example.frapizza.dao.impl;

import com.example.frapizza.dao.PizzeriaDao;
import com.example.frapizza.entity.Pizzeria;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PizzeriaDaoImpl implements PizzeriaDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(PizzeriaDaoImpl.class.getName());
  private final PgPool pool;

  public PizzeriaDaoImpl(PgPool pool) {
    this.pool = pool;
  }

  @Override
  public void save(Pizzeria pizzeria, Handler<AsyncResult<Void>> resultHandler) {
    String insertQuery = "INSERT INTO pizzerias(city, street, building, latitude, longitude) " +
      "VALUES ($1, $2, $3, $4, $5) RETURNING id";
    pool.withTransaction(client -> client
        .preparedQuery(insertQuery)
        .execute(Tuple.of(pizzeria.getCity(), pizzeria.getStreet(), pizzeria.getBuilding(),
          pizzeria.getLatitude(), pizzeria.getLongitude()))
        .map(rs -> rs.iterator().next().getInteger("id")))
      .onSuccess(id -> {
        LOGGER.info("Transaction succeeded: pizzeria is created " + id);
        resultHandler.handle(Future.succeededFuture());
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: pizzeria not created " + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
  }

  @Override
  public void update(Integer id, Pizzeria pizzeria, Handler<AsyncResult<Void>> resultHandler) {
    String updateQuery = "UPDATE pizzerias SET(city, street, building, latitude, longitude) " +
      "= ($1, $2, $3, $4, $5) WHERE id=$6";
    pool.withTransaction(client -> client
        .preparedQuery(updateQuery)
        .execute(Tuple.of(pizzeria.getCity(), pizzeria.getStreet(), pizzeria.getBuilding(),
          pizzeria.getLatitude(), pizzeria.getLongitude(), id)))
      .onSuccess(rs -> {
        LOGGER.warn("Transaction succeeded: pizzeria is updated " + id);
        resultHandler.handle(Future.succeededFuture());
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: pizzeria not updated " + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
  }

  @Override
  public void delete(Integer id, Handler<AsyncResult<Void>> resultHandler) {
    String deleteQuery = "DELETE FROM pizzerias WHERE id=$1";
    pool.withTransaction(client -> client
        .preparedQuery(deleteQuery)
        .execute(Tuple.of(id)))
      .onSuccess(rs -> {
        LOGGER.warn("Transaction succeeded: pizzeria is deleted " + id);
        resultHandler.handle(Future.succeededFuture());
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: pizzeria not deleted " + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
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
