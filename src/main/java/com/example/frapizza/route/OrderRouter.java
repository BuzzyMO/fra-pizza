package com.example.frapizza.route;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public interface OrderRouter {

  static Router create(Vertx vertx) {
    return new OrderRoute(vertx).getRouter();
  }

  void save(RoutingContext routingContext);

  void delete(RoutingContext routingContext);

  void readAll(RoutingContext routingContext);

  Router getRouter();
}
