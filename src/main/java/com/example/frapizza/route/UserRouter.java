package com.example.frapizza.route;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.SessionHandler;

public interface UserRouter {

  static Router create(Vertx vertx, SessionHandler sessionHandler) {
    return new UserRoute(vertx, sessionHandler).getRouter();
  }

  void save(RoutingContext routingContext);

  void update(RoutingContext routingContext);

  void delete(RoutingContext routingContext);

  void readUserPizzas(RoutingContext routingContext);

  void readUserOrders(RoutingContext routingContext);

  void readUserAuthorities(RoutingContext routingContext);

  void readAll(RoutingContext routingContext);

  Router getRouter();
}
