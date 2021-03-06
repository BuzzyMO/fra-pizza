package com.example.frapizza.route;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.SessionHandler;

public interface IngredientRouter {

  static Router create(Vertx vertx, SessionHandler sessionHandler) {
    return new IngredientRoute(vertx, sessionHandler).getRouter();
  }

  void save(RoutingContext routingContext);

  void update(RoutingContext routingContext);

  void delete(RoutingContext routingContext);

  void readAll(RoutingContext routingContext);

  Router getRouter();
}
