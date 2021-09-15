package com.example.frapizza.route;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public interface AuthorityRouter {

  static Router create(Vertx vertx) {
    return new AuthorityRoute(vertx).getRouter();
  }

  Router getRouter();
}
