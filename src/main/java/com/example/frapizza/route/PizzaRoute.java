package com.example.frapizza.route;

import com.example.frapizza.service.PizzaService;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PizzaRoute implements PizzaRouter {
  private static final Logger LOGGER = LoggerFactory.getLogger(PizzaRoute.class.getName());
  private final Router router;
  private final PizzaService pizzaService;

  public PizzaRoute(Vertx vertx) {
    pizzaService = PizzaService.createProxy(vertx, PizzaService.ADDRESS);
    this.router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.get("/save").handler(this::save);
  }

  private void save(RoutingContext routingContext) {
    Long userId = routingContext.user().get("id");
    JsonObject pizzaIngredientsJson = routingContext.getBodyAsJson();
    pizzaIngredientsJson.getJsonObject("pizza").put("createdBy", userId);

    pizzaService.save(pizzaIngredientsJson, ar -> {
      if (ar.succeeded()) {
        LOGGER.info("Pizza is created");
        routingContext.response().setStatusCode(201).end();
      } else {
        LOGGER.error("Pizza not created");
        routingContext.response().setStatusCode(400).end();
      }
    });
  }

  public Router getRouter() {
    return router;
  }
}
