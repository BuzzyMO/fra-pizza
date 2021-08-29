package com.example.frapizza.service;

import com.example.frapizza.service.impl.OrderServiceImpl;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

@VertxGen
@ProxyGen
public interface OrderService {
  String ADDRESS = "order.service";

  static OrderService create(Vertx vertx) {
    return new OrderServiceImpl(vertx);
  }

  static OrderService createProxy(Vertx vertx, String address) {
    return new OrderServiceVertxEBProxy(vertx, address);
  }

  void save(JsonObject deliveryPizzas, Handler<AsyncResult<Void>> resultHandler);
}
