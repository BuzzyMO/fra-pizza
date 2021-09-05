package com.example.frapizza.route;

import com.example.frapizza.service.UserService;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRoute implements UserRouter {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserRoute.class.getName());
  private final Router router;
  private final UserService userService;

  public UserRoute(Vertx vertx) {
    userService = UserService.createProxy(vertx, UserService.ADDRESS);
    this.router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.post().handler(this::save);
    router.put("/:id").handler(this::update);
    router.delete("/:id").handler(this::delete);
    router.get().handler(this::readAll);
  }

  private void save(RoutingContext routingContext) {
    userService.save(routingContext.getBodyAsJson(), ar -> {
      if (ar.succeeded()) {
        LOGGER.info("User is created");
        routingContext.response().setStatusCode(201).end();
      } else {
        LOGGER.error("User not created " + ar.cause().getMessage());
        routingContext.response().setStatusCode(400).end();
      }
    });
  }

  private void update(RoutingContext routingContext) {
    String idStr = routingContext.pathParam("id");
    Long id = Long.parseLong(idStr);
    userService.update(id, routingContext.getBodyAsJson(), ar -> {
      if (ar.succeeded()) {
        LOGGER.warn("User is updated");
        routingContext.response().setStatusCode(200).end();
      } else {
        LOGGER.error("User not updated: " + ar.cause().getMessage());
        routingContext.response().setStatusCode(400).end();
      }
    });
  }

  private void delete(RoutingContext routingContext) {
    String idStr = routingContext.pathParam("id");
    Long id = Long.parseLong(idStr);
    userService.delete(id, ar -> {
      if (ar.succeeded()) {
        LOGGER.warn("User is deleted");
        routingContext.response().setStatusCode(204).end();
      } else {
        LOGGER.error("User not deleted: " + ar.cause().getMessage());
        routingContext.response().setStatusCode(400).end();
      }
    });
  }

  private void readAll(RoutingContext routingContext) {
    userService.readAll(ar -> {
      if (ar.succeeded()) {
        LOGGER.info("Users is read");
        routingContext.response()
          .setStatusCode(200)
          .putHeader("Content-Type", "application/json")
          .end(ar.result().toBuffer());
      } else {
        LOGGER.error("Users not read: " + ar.cause().getMessage());
        routingContext.response().setStatusCode(400).end();
      }
    });
  }

  @Override
  public Router getRouter() {
    return router;
  }

}
