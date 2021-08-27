package com.example.frapizza.route;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public interface UserRouter {

  static Router create(Vertx vertx) {
    return new UserRoute(vertx).getRouter();
  }

  Router getRouter();
}
