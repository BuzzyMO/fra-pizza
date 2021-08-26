package com.example.frapizza.service;

import com.example.frapizza.service.impl.AuthServiceImpl;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

@VertxGen
@ProxyGen
public interface AuthService {
  String ADDRESS = "auth.service";

  static AuthService create(Vertx vertx) {
    return new AuthServiceImpl(vertx);
  }

  static AuthService createProxy(Vertx vertx, String address) {
    return new AuthServiceVertxEBProxy(vertx, address);
  }

  void authentication(JsonObject credentials, Handler<AsyncResult<JsonObject>> resultHandler);
}
