package com.example.frapizza.dao;

import com.example.frapizza.dao.impl.UserDaoImpl;
import com.example.frapizza.entity.User;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.pgclient.PgPool;

@VertxGen
@ProxyGen
public interface UserDao {
  String ADDRESS = "user.dao";

  static UserDao create(PgPool pool) {
    return new UserDaoImpl(pool);
  }

  static UserDao createProxy(Vertx vertx, String address) {
    return new UserDaoVertxEBProxy(vertx, address);
  }

  void save(User user, Handler<AsyncResult<Void>> resultHandler);

  void update(Long id, User user, Handler<AsyncResult<Void>> resultHandler);

  void delete(Long id, Handler<AsyncResult<Void>> resultHandler);

  void readAll(Handler<AsyncResult<JsonArray>> resultHandler);
}
