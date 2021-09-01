package com.example.frapizza.service;

import com.example.frapizza.service.impl.UserServiceImpl;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@VertxGen
@ProxyGen
public interface UserService {
  String ADDRESS = "user.service";

  static UserService create(Vertx vertx) {
    return new UserServiceImpl(vertx);
  }

  static UserService createProxy(Vertx vertx, String address) {
    return new UserServiceVertxEBProxy(vertx, address);
  }

  void save(JsonObject userJson, Handler<AsyncResult<Void>> resultHandler);
  void update(JsonObject userJson, Handler<AsyncResult<Void>> resultHandler);
  void delete(Long id, Handler<AsyncResult<Void>> resultHandler);
  void readAll(Handler<AsyncResult<JsonArray>> resultHandler);

}
