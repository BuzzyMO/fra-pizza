package com.example.frapizza.dao;

import com.example.frapizza.dao.impl.AuthDaoImpl;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgPool;

@VertxGen
@ProxyGen
public interface AuthDao {
  String ADDRESS = "auth.dao";

  static AuthDao create(PgPool pool) {
    return new AuthDaoImpl(pool);
  }

  static AuthDao createProxy(Vertx vertx, String address) {
    return new AuthDaoVertxEBProxy(vertx, address);
  }

  void authentication(JsonObject credentials, Handler<AsyncResult<JsonObject>> resultHandler);
}
