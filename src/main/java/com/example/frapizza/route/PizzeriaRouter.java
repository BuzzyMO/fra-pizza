package com.example.frapizza.route;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public interface PizzeriaRouter {
  static Router create(Vertx vertx) {
    return new PizzeriaRoute(vertx).getRouter();
  }

  Router getRouter();
}
