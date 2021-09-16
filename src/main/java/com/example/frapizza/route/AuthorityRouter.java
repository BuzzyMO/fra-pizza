package com.example.frapizza.route;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public interface AuthorityRouter {

  static Router create(Vertx vertx) {
    return new AuthorityRoute(vertx).getRouter();
  }

  void readAuthorityPizzas(RoutingContext routingContext);

  Router getRouter();
}
