package com.example.frapizza.service;

import com.example.frapizza.service.impl.PizzaServiceImpl;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
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
  void update(Long id, JsonObject pizzaJson, Handler<AsyncResult<Void>> resultHandler);
  void delete(Long id, Handler<AsyncResult<Void>> resultHandler);
  void readByAuthority(Integer authorityId, Handler<AsyncResult<JsonArray>> resultHandler);
  void readByUser(Long userId, Handler<AsyncResult<JsonArray>> resultHandler);
  void readAll(Handler<AsyncResult<JsonArray>> resultHandler);
}
