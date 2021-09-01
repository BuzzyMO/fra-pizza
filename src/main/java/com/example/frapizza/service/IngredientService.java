package com.example.frapizza.service;

import com.example.frapizza.service.impl.IngredientServiceImpl;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@VertxGen
@ProxyGen
public interface IngredientService {
  String ADDRESS = "ingredient.service";

  static IngredientService create(Vertx vertx) {
    return new IngredientServiceImpl(vertx);
  }

  static IngredientService createProxy(Vertx vertx, String address) {
    return new IngredientServiceVertxEBProxy(vertx, address);
  }

  void save(JsonObject ingredientJson, Handler<AsyncResult<Void>> resultHandler);
  void update(JsonObject ingredientJson, Handler<AsyncResult<Void>> resultHandler);
  void delete(Integer id, Handler<AsyncResult<Void>> resultHandler);
  void readAll(Handler<AsyncResult<JsonArray>> resultHandler);

}
