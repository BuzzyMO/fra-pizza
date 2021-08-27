package com.example.frapizza.route;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public interface PizzaRouter {

  static Router create(Vertx vertx) {
    return new PizzaRoute(vertx).getRouter();
  }

  Router getRouter();
}
