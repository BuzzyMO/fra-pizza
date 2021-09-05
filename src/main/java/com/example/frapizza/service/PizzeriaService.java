package com.example.frapizza.service;

import com.example.frapizza.service.impl.PizzeriaServiceImpl;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@VertxGen
@ProxyGen
public interface PizzeriaService {
  String ADDRESS = "pizzeria.service";

  static PizzeriaService create(Vertx vertx) {
    return new PizzeriaServiceImpl(vertx);
  }

  static PizzeriaService createProxy(Vertx vertx, String address) {
    return new PizzeriaServiceVertxEBProxy(vertx, address);
  }

  void save(JsonObject pizzeriaJson, Handler<AsyncResult<Void>> resultHandler);
  void update(Integer id, JsonObject pizzeriaJson, Handler<AsyncResult<Void>> resultHandler);
  void delete(Integer id, Handler<AsyncResult<Void>> resultHandler);
  void readAll(Handler<AsyncResult<JsonArray>> resultHandler);
}
