package com.example.frapizza.service;

import com.example.frapizza.dao.impl.UserDaoImpl;
import com.example.frapizza.service.impl.UserServiceImpl;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgPool;

@VertxGen
@ProxyGen
public interface UserService {
  String ADDRESS = "user.service";

  static UserService create(Vertx vertx, PgPool pool) {
    return new UserServiceImpl(vertx, pool);
  }

  static UserService createProxy(Vertx vertx, String address) {
    return new UserServiceVertxEBProxy(vertx, address);
  }

  void save(JsonObject userJson, Handler<AsyncResult<Void>> resultHandler);
  void update(JsonObject userJson, Handler<AsyncResult<JsonObject>> resultHandler);
  void delete(String id, Handler<AsyncResult<JsonObject>> resultHandler);
  void readById(String id, Handler<AsyncResult<JsonObject>> resultHandler);
  void readAll(Handler<AsyncResult<JsonObject>> resultHandler);

}
