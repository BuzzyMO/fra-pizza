package com.example.frapizza.dao.impl;

import com.example.frapizza.dao.OrderDao;
import com.example.frapizza.entity.Delivery;
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

public class OrderDaoImpl implements OrderDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(OrderDaoImpl.class.getName());
  private final PgPool pool;

  public OrderDaoImpl(PgPool pool) {
    this.pool = pool;
  }

  @Override
  public void save(JsonObject deliveryPizzas, Handler<AsyncResult<Void>> resultHandler) {

    Delivery delivery = deliveryPizzas.getJsonObject("delivery").mapTo(Delivery.class);
    List<Integer> pizzaIds = deliveryPizzas.getJsonArray("pizzas")
      .stream()
      .map(e -> (Integer) e)
      .collect(Collectors.toList());

    String insertDeliveryQuery = "INSERT INTO deliveries(created_by, pizzeria_from, city, street, building, apartment, distance_m, exp_time) " +
      "VALUES ($1, $2, $3, $4, $5, $6, $7, $8) RETURNING id";
    pool.withTransaction(client -> client
        .preparedQuery(insertDeliveryQuery)
        .execute(Tuple.of(delivery.getCreatedBy(), delivery.getPizzeriaFrom(), delivery.getCity(),
          delivery.getStreet(), delivery.getBuilding(), delivery.getApartment(),
          delivery.getDistanceM(), delivery.getExpTime()))
        .map(rs -> rs.iterator().next().getLong("id"))
        .compose(id -> saveOrder(client, id, pizzaIds)))
      .onSuccess(rs -> {
        LOGGER.info("Transaction succeeded: order is created");
        resultHandler.handle(Future.succeededFuture());
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed: order not created" + ex.getMessage());
        resultHandler.handle(Future.failedFuture(ex));
      });
  }

  private Future<RowSet<Row>> saveOrder(SqlConnection client, Long deliveryId, List<Integer> pizzaIds) {
    List<Tuple> batch = new ArrayList<>();
    for (Integer i : pizzaIds) {
      batch.add(Tuple.of(deliveryId, i));
    }

    String insertOrderQuery = "INSERT INTO orders(delivery_id, pizza_id) " +
      "VALUES ($1, $2)";
    return client
      .preparedQuery(insertOrderQuery)
      .executeBatch(batch);
  }
}
