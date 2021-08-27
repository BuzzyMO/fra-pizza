package com.example.frapizza.service;

import com.example.frapizza.service.impl.PizzaServiceImpl;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

@VertxGen
@ProxyGen
public interface PizzaService {
  String ADDRESS = "pizza.service";

  static PizzaService create(Vertx vertx) {
    return new PizzaServiceImpl(vertx);
  }

  static PizzaService createProxy(Vertx vertx, String address) {
    return new PizzaServiceVertxEBProxy(vertx, address);
  }

  void save(JsonObject completePizza, Handler<AsyncResult<Void>> resultHandler);
}
