package com.example.frapizza.route;

import com.example.frapizza.service.PizzaService;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.authorization.RoleBasedAuthorization;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PizzaRoute implements PizzaRouter {
  private static final Logger LOGGER = LoggerFactory.getLogger(PizzaRoute.class.getName());
  private final Router router;
  private final PizzaService pizzaService;

  public PizzaRoute(Vertx vertx) {
    pizzaService = PizzaService.createProxy(vertx, PizzaService.ADDRESS);
    AuthenticationHandler authHandler = AuthHandler.createAuthenticationHandler(vertx);
    AuthorizationHandler authorizationAdminHandler = AuthorizationHandler
      .create(RoleBasedAuthorization.create("ROLE_ADMIN"));
    this.router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.post()
      .handler(authHandler)
      .handler(this::save);
    router.put("/:id")
      .handler(authHandler)
      .handler(authorizationAdminHandler)
      .handler(this::update);
    router.delete("/:id")
      .handler(authHandler)
      .handler(authorizationAdminHandler)
      .handler(this::delete);
    router.get()
      .handler(authHandler)
      .handler(authorizationAdminHandler)
      .handler(this::readAll);
  }

  @Override
  public void save(RoutingContext routingContext) {
    Long userId = routingContext.user().principal().getLong("id");

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

  @Override
  public void update(RoutingContext routingContext) {
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

  @Override
  public void delete(RoutingContext routingContext) {
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

  @Override
  public void readAll(RoutingContext routingContext) {
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
