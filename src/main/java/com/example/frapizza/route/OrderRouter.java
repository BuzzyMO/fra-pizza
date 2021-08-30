package com.example.frapizza.route;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public interface OrderRouter {

  static Router create(Vertx vertx) {
    return new OrderRoute(vertx).getRouter();
  }

  Router getRouter();
}
