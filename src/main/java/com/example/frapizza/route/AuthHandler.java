package com.example.frapizza.route;

import io.vertx.core.Vertx;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.BasicAuthHandler;

public interface AuthHandler {

  static AuthenticationHandler createAuthenticationHandler(Vertx vertx) {
    return BasicAuthHandler.create(new AuthenticationProviderImpl(vertx));
  }
}
