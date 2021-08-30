package com.example.frapizza.route;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public interface IngredientRouter {

  static Router create(Vertx vertx){
    return new IngredientRoute(vertx).getRouter();
  }

  Router getRouter();
}
