package com.example.frapizza.route;

import com.example.frapizza.service.OrderService;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderRoute implements OrderRouter{
  private static final Logger LOGGER = LoggerFactory.getLogger(OrderRoute.class.getName());
  private final Router router;
  private final OrderService orderService;

  public OrderRoute(Vertx vertx) {
    this.orderService = OrderService.createProxy(vertx, OrderService.ADDRESS);
    this.router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.get("/save").handler(this::save);
  }

  private void save(RoutingContext routingContext) {
    Integer userId = routingContext.user().get("id");
    JsonObject deliveryPizzasJson = routingContext.getBodyAsJson();
    deliveryPizzasJson.getJsonObject("delivery").put("createdBy", userId);

    orderService.save(deliveryPizzasJson, ar -> {
      if (ar.succeeded()) {
        LOGGER.info("Order is created");
        routingContext.response().setStatusCode(201).end();
      } else {
        LOGGER.error("Order not created: " + ar.cause().getMessage());
        routingContext.response().setStatusCode(400).end();
      }
    });
  }

  public Router getRouter() {
    return router;
  }
}
