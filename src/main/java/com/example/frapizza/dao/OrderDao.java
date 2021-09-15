package com.example.frapizza.dao;

import com.example.frapizza.dao.impl.OrderDaoImpl;
import com.example.frapizza.entity.Delivery;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.pgclient.PgPool;

import java.util.List;

@VertxGen
@ProxyGen
public interface OrderDao {
  String ADDRESS = "order.dao";

  static OrderDao create(PgPool pool) {
    return new OrderDaoImpl(pool);
  }

  static OrderDao createProxy(Vertx vertx, String address) {
    return new OrderDaoVertxEBProxy(vertx, address);
  }

  void save(Delivery delivery, List<Integer> pizzaIds, Handler<AsyncResult<Void>> resultHandler);

  void delete(Long id, Handler<AsyncResult<Void>> resultHandler);

  void readByCurrentUser(Long userId, Handler<AsyncResult<JsonArray>> resultHandler);

  void readAll(Handler<AsyncResult<JsonArray>> resultHandler);
}
