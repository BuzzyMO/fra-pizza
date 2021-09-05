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
    router.post().handler(this::save);
    router.put("/:id").handler(this::update);
    router.delete("/:id").handler(this::delete);
    router.get("/authority/:authority").handler(this::readByAuthority);
    router.get("/user").handler(this::readByUser);
    router.get().handler(this::readAll);
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
        LOGGER.error("Pizza not created " + ar.cause().getMessage());
        routingContext.response().setStatusCode(400).end();
      }
    });
  }
  private void update(RoutingContext routingContext){
    String idStr = routingContext.pathParam("id");
    Long id = Long.parseLong(idStr);
    pizzaService.update(id, routingContext.getBodyAsJson(), ar -> {
      if (ar.succeeded()) {
        LOGGER.warn("Pizza is updated");
        routingContext.response().setStatusCode(200).end();
      } else {
        LOGGER.error("Pizza not updated " + ar.cause().getMessage());
        routingContext.response().setStatusCode(400).end();
      }
    });
  }
  private void delete(RoutingContext routingContext){
    String idStr = routingContext.pathParam("id");
    Long id = Long.parseLong(idStr);
    pizzaService.delete(id, ar -> {
      if (ar.succeeded()) {
        LOGGER.warn("Pizza is deleted");
        routingContext.response().setStatusCode(204).end();
      } else {
        LOGGER.error("Pizza not deleted " + ar.cause().getMessage());
        routingContext.response().setStatusCode(400).end();
      }
    });
  }
  private void readByAuthority(RoutingContext routingContext){
    String authorityStr = routingContext.pathParam("authority");
    Integer id = Integer.parseInt(authorityStr);
    pizzaService.readByAuthority(id, ar -> {
      if (ar.succeeded()) {
        LOGGER.info("Pizzas is read by " + authorityStr);
        routingContext.response()
          .setStatusCode(200)
          .putHeader("Content-Type", "application/json")
          .end(ar.result().toBuffer());
      } else {
        LOGGER.error("Pizzas not read by authority " + ar.cause().getMessage());
        routingContext.response().setStatusCode(400).end();
      }
    });
  }


  private void readByUser(RoutingContext routingContext){
    Long userId = routingContext.user().get("id");
    pizzaService.readByUser(userId, ar -> {
      if (ar.succeeded()) {
        LOGGER.info("Pizzas is read by user " + userId);
        routingContext.response()
          .setStatusCode(200)
          .putHeader("Content-Type", "application/json")
          .end(ar.result().toBuffer());
      } else {
        LOGGER.error("Pizzas not read by user " + ar.cause().getMessage());
        routingContext.response().setStatusCode(400).end();
      }
    });
  }

  private void readAll(RoutingContext routingContext){
    pizzaService.readAll(ar -> {
      if (ar.succeeded()) {
        LOGGER.info("Pizzas is read");
        routingContext.response()
          .setStatusCode(200)
          .putHeader("Content-Type", "application/json")
          .end(ar.result().toBuffer());
      } else {
        LOGGER.error("Pizzas not read: " + ar.cause().getMessage());
        routingContext.response().setStatusCode(400).end();
      }
    });
  }

  public Router getRouter() {
    return router;
  }
}
