package com.example.frapizza.dao;

import com.example.frapizza.dao.impl.OrderDaoImpl;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgPool;

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

  void save(JsonObject deliveryPizzas, Handler<AsyncResult<Void>> resultHandler);
}
