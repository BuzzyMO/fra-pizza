package com.example.frapizza.dao;

import com.example.frapizza.dao.impl.PizzeriaDaoImpl;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgPool;

@VertxGen
@ProxyGen
public interface PizzeriaDao {
  String ADDRESS = "pizzeria.dao";

  static PizzeriaDao create(PgPool pool) {
    return new PizzeriaDaoImpl(pool);
  }

  static PizzeriaDao createProxy(Vertx vertx, String address) {
    return new PizzeriaDaoVertxEBProxy(vertx, address);
  }

  void save(JsonObject pizzeriaJson, Handler<AsyncResult<Void>> resultHandler);
  void readAll(Handler<AsyncResult<JsonArray>> resultHandler);
}
