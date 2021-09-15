package com.example.frapizza.dao.impl;

import com.example.frapizza.dao.OrderDao;
import com.example.frapizza.entity.Delivery;
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

public class OrderDaoImpl implements OrderDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(OrderDaoImpl.class.getName());
  private final PgPool pool;

  public OrderDaoImpl(PgPool pool) {
    this.pool = pool;
  }

  @Override
  public void save(Delivery delivery, List<Integer> pizzaIds, Handler<AsyncResult<Void>> resultHandler) {
    String insertDeliveryQuery = "INSERT INTO deliveries(created_by, pizzeria_from, city, street, building, apartment, distance_m, exp_time) " +
      "VALUES ($1, $2, $3, $4, $5, $6, $7, $8) RETURNING id";
    pool.withTransaction(client -> client
        .preparedQuery(insertDeliveryQuery)
        .execute(Tuple.of(delivery.getCreatedBy(), delivery.getPizzeriaFrom(), delivery.getCity(),
          delivery.getStreet(), delivery.getBuilding(), delivery.getApartment(),
          delivery.getDistanceM(), delivery.getExpTime()))
        .map(rs -> rs.iterator().next().getLong("id"))
        .compose(id -> saveOrder(client, id, pizzaIds))
        .map(rs -> rs.iterator().next().getLong("id")))
      .onSuccess(id -> {
        LOGGER.info("Transaction succeeded: order is created " + id);
        resultHandler.handle(Future.succeededFuture());
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: order not created" + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
  }

  @Override
  public void delete(Long id, Handler<AsyncResult<Void>> resultHandler) {
    String deleteQuery = "DELETE FROM orders WHERE id=$1";
    pool.withTransaction(client -> client
        .preparedQuery(deleteQuery)
        .execute(Tuple.of(id)))
      .onSuccess(rs -> {
        LOGGER.warn("Transaction succeeded: order is deleted " + id);
        resultHandler.handle(Future.succeededFuture());
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: order not deleted " + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
  }

  @Override
  public void readByCurrentUser(Long userId, Handler<AsyncResult<JsonArray>> resultHandler) {
    String readAllQuery = "SELECT o.id as order_id, ordered_at, p.name as pizza_name, pizzeria_from, " +
      "city, street, building, apartment, distance_m, exp_time " +
      "FROM orders o " +
      "INNER JOIN deliveries d on d.id = o.delivery_id " +
      "INNER JOIN pizzas p on p.id = o.pizza_id " +
      "WHERE d.created_by = $1";
    pool.withTransaction(client -> client
        .preparedQuery(readAllQuery)
        .execute(Tuple.of(userId)))
      .onSuccess(rs -> {
        JsonArray jsonArray = new JsonArray();
        for (Row row : rs) {
          jsonArray.add(row.toJson());
        }
        LOGGER.info("Transaction succeeded: orders is read by user " + userId);
        resultHandler.handle(Future.succeededFuture(jsonArray));
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: orders not read: " + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
  }

  @Override
  public void readAll(Handler<AsyncResult<JsonArray>> resultHandler) {
    String readAllQuery = "SELECT* FROM orders INNER JOIN deliveries d on d.id = orders.delivery_id";
    pool.withTransaction(client -> client
        .preparedQuery(readAllQuery)
        .execute())
      .onSuccess(rs -> {
        JsonArray jsonArray = new JsonArray();
        for (Row row : rs) {
          jsonArray.add(row.toJson());
        }
        LOGGER.info("Transaction succeeded: orders is read");
        resultHandler.handle(Future.succeededFuture(jsonArray));
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: orders not read: " + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
  }

  private Future<RowSet<Row>> saveOrder(SqlConnection client, Long deliveryId, List<Integer> pizzaIds) {
    List<Tuple> batch = new ArrayList<>();
    for (Integer i : pizzaIds) {
      batch.add(Tuple.of(deliveryId, i));
    }

    String insertOrderQuery = "INSERT INTO orders(delivery_id, pizza_id) " +
      "VALUES ($1, $2) RETURNING id";
    return client
      .preparedQuery(insertOrderQuery)
      .executeBatch(batch);
  }
}
