package com.example.frapizza.route;

import com.example.frapizza.service.OrderService;
import com.example.frapizza.service.PizzaService;
import com.example.frapizza.service.UserService;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.auth.authorization.RoleBasedAuthorization;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRoute implements UserRouter {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserRoute.class.getName());
  private final Router router;
  private final UserService userService;
  private final PizzaService pizzaService;
  private final OrderService orderService;

  public UserRoute(Vertx vertx) {
    userService = UserService.createProxy(vertx, UserService.ADDRESS);
    pizzaService = PizzaService.createProxy(vertx, PizzaService.ADDRESS);
    this.orderService = OrderService.createProxy(vertx, OrderService.ADDRESS);
    AuthenticationHandler authHandler = AuthHandler.createAuthenticationHandler(vertx);
    AuthorizationHandler authorizationAdminHandler = AuthorizationHandler
      .create(RoleBasedAuthorization.create("ROLE_ADMIN"));
    this.router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.post().handler(this::save);
    router.put("/:id")
      .handler(authHandler)
      .handler(this::update);
    router.delete("/:id")
      .handler(authHandler)
      .handler(authorizationAdminHandler)
      .handler(this::delete);
    router.get("/authorities")
      .handler(authHandler)
      .handler(authorizationAdminHandler)
      .handler(this::readUserAuthorities);
    router.get("/pizzas")
      .handler(authHandler)
      .handler(this::readUserPizzas);
    router.get("/orders")
      .handler(authHandler)
      .handler(this::readUserOrders);
    router.get()
      .handler(authHandler)
      .handler(authorizationAdminHandler)
      .handler(this::readAll);
  }

  @Override
  public void save(RoutingContext routingContext) {
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

  @Override
  public void update(RoutingContext routingContext) {
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

  @Override
  public void delete(RoutingContext routingContext) {
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

  @Override
  public void readUserPizzas(RoutingContext routingContext) {
    Long userId = routingContext.user().principal().getLong("id");

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

  @Override
  public void readUserOrders(RoutingContext routingContext) {
    Long userId = routingContext.user().principal().getLong("id");

    orderService.readByCurrentUser(userId, ar -> {
      if (ar.succeeded()) {
        LOGGER.info("Orders is read by current user");
        routingContext.response()
          .setStatusCode(200)
          .putHeader("Content-Type", "application/json")
          .end(ar.result().toBuffer());
      } else {
        LOGGER.error("Orders not read by user: " + ar.cause().getMessage());
        routingContext.response().setStatusCode(400).end();
      }
    });
  }

  @Override
  public void readUserAuthorities(RoutingContext routingContext) {
    JsonArray roles = routingContext.user()
      .principal()
      .getJsonArray("roles");
    routingContext.response()
      .setStatusCode(200)
      .putHeader("Content-Type", "application/json")
      .end(roles.toBuffer());
  }

  @Override
  public void readAll(RoutingContext routingContext) {
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
