package com.example.frapizza.route;

import com.example.frapizza.service.PizzeriaService;
import io.vertx.core.Vertx;
import io.vertx.ext.auth.authorization.RoleBasedAuthorization;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PizzeriaRoute implements PizzeriaRouter {
  private static final Logger LOGGER = LoggerFactory.getLogger(PizzeriaRoute.class.getName());
  private final Router router;
  private final PizzeriaService pizzeriaService;

  public PizzeriaRoute(Vertx vertx) {
    this.pizzeriaService = PizzeriaService.createProxy(vertx, PizzeriaService.ADDRESS);
    AuthenticationHandler authHandler = AuthHandler.createAuthenticationHandler(vertx);
    AuthorizationHandler authorizationAdminHandler = AuthorizationHandler
      .create(RoleBasedAuthorization.create("ROLE_ADMIN"));
    this.router = Router.router(vertx);
    router.route()
      .handler(BodyHandler.create())
      .handler(authHandler)
      .handler(authorizationAdminHandler);
    router.post().handler(this::save);
    router.put("/:id").handler(this::update);
    router.delete("/:id").handler(this::delete);
    router.get().handler(this::readAll);
  }

  @Override
  public void save(RoutingContext routingContext) {
    pizzeriaService.save(routingContext.getBodyAsJson(), ar -> {
      if (ar.succeeded()) {
        LOGGER.info("Pizzeria is created");
        routingContext.response().setStatusCode(201).end();
      } else {
        LOGGER.error("Pizzeria not created: " + ar.cause().getMessage());
        routingContext.response().setStatusCode(400).end();
      }
    });
  }

  @Override
  public void update(RoutingContext routingContext) {
    String idStr = routingContext.pathParam("id");
    Integer id = Integer.parseInt(idStr);
    pizzeriaService.update(id, routingContext.getBodyAsJson(), ar -> {
      if (ar.succeeded()) {
        LOGGER.warn("Pizzeria is updated");
        routingContext.response().setStatusCode(200).end();
      } else {
        LOGGER.error("Pizzeria not updated: " + ar.cause().getMessage());
        routingContext.response().setStatusCode(400).end();
      }
    });
  }

  @Override
  public void delete(RoutingContext routingContext) {
    String idStr = routingContext.pathParam("id");
    Integer id = Integer.parseInt(idStr);
    pizzeriaService.delete(id, ar -> {
      if (ar.succeeded()) {
        LOGGER.warn("Pizzeria is deleted");
        routingContext.response().setStatusCode(204).end();
      } else {
        LOGGER.error("Pizzeria not deleted: " + ar.cause().getMessage());
        routingContext.response().setStatusCode(400).end();
      }
    });
  }

  @Override
  public void readAll(RoutingContext routingContext) {
    pizzeriaService.readAll(ar -> {
      if (ar.succeeded()) {
        LOGGER.info("Pizzerias is read");
        routingContext.response()
          .setStatusCode(200)
          .putHeader("Content-Type", "application/json")
          .end(ar.result().toBuffer());
      } else {
        LOGGER.error("Pizzerias not read: " + ar.cause().getMessage());
        routingContext.response().setStatusCode(400).end();
      }
    });
  }

  @Override
  public Router getRouter() {
    return router;
  }
}
