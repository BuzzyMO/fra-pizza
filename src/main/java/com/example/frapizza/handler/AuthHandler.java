package com.example.frapizza.handler;

import io.vertx.core.Vertx;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.BasicAuthHandler;

public interface AuthHandler {

  static AuthenticationHandler createAuthHandler(Vertx vertx){
    return BasicAuthHandler.create(new AuthenticationProviderImpl(vertx));
  }
}
