package com.example.frapizza.route;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.SessionStore;

public interface AuthRouter {

  static Router create(Vertx vertx, SessionHandler sessionHandler, SessionStore sessionStore) {
    return new AuthRoute(vertx, sessionHandler, sessionStore).getRouter();
  }

  Router getRouter();
}
