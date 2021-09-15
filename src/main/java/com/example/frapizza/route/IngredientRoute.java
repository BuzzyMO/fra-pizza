package com.example.frapizza.route;

import com.example.frapizza.service.IngredientService;
import io.vertx.core.Vertx;
import io.vertx.ext.auth.authorization.RoleBasedAuthorization;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IngredientRoute implements IngredientRouter {
  private static final Logger LOGGER = LoggerFactory.getLogger(IngredientRoute.class.getName());
  private final Router router;
  private final IngredientService ingredientService;

  public IngredientRoute(Vertx vertx) {
    this.ingredientService = IngredientService.createProxy(vertx, IngredientService.ADDRESS);
    AuthenticationHandler authHandler = AuthHandler.createAuthenticationHandler(vertx);
    AuthorizationHandler authorizationAdminHandler = AuthorizationHandler
      .create(RoleBasedAuthorization.create("ROLE_ADMIN"));
    this.router = Router.router(vertx);
    router.route()
      .handler(BodyHandler.create())
      .handler(authHandler);
    router.post()
      .handler(authorizationAdminHandler)
      .handler(this::save);
    router.put("/:id")
      .handler(authorizationAdminHandler)
      .handler(this::update);
    router.delete("/:id")
      .handler(authorizationAdminHandler)
      .handler(this::delete);
    router.get().handler(this::readAll);
  }

  private void save(RoutingContext routingContext) {
    ingredientService.save(routingContext.getBodyAsJson(), ar -> {
      if (ar.succeeded()) {
        LOGGER.info("Ingredient is created");
        routingContext.response().setStatusCode(201).end();
      } else {
        LOGGER.error("Ingredient not created: " + ar.cause().getMessage());
        routingContext.response().setStatusCode(400).end();
      }
    });
  }

  private void update(RoutingContext routingContext) {
    String idStr = routingContext.pathParam("id");
    Integer id = Integer.parseInt(idStr);
    ingredientService.update(id, routingContext.getBodyAsJson(), ar -> {
      if (ar.succeeded()) {
        LOGGER.warn("Ingredient is updated");
        routingContext.response().setStatusCode(200).end();
      } else {
        LOGGER.error("Ingredient not updated: " + ar.cause().getMessage());
        routingContext.response().setStatusCode(400).end();
      }
    });
  }

  private void delete(RoutingContext routingContext) {
    String idStr = routingContext.pathParam("id");
    Integer id = Integer.parseInt(idStr);
    ingredientService.delete(id, ar -> {
      if (ar.succeeded()) {
        LOGGER.warn("Ingredient is deleted");
        routingContext.response().setStatusCode(204).end();
      } else {
        LOGGER.error("Ingredient not deleted: " + ar.cause().getMessage());
        routingContext.response().setStatusCode(400).end();
      }
    });
  }

  private void readAll(RoutingContext routingContext) {
    ingredientService.readAll(ar -> {
      if (ar.succeeded()) {
        LOGGER.info("Ingredients is read");
        routingContext.response()
          .setStatusCode(200)
          .putHeader("Content-Type", "application/json")
          .end(ar.result().toBuffer());
      } else {
        LOGGER.error("Ingredients not read: " + ar.cause().getMessage());
        routingContext.response().setStatusCode(400).end();
      }
    });
  }

  public Router getRouter() {
    return router;
  }
}
