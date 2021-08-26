package com.example.frapizza.handler;

import com.example.frapizza.service.UserService;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserHandler.class.getName());
  private final Router router;
  private final UserService userService;

  public UserHandler(Vertx vertx){
    userService = UserService.createProxy(vertx, UserService.ADDRESS);
    this.router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.get("/save").handler(this::save);
  }

  private void save(RoutingContext routingContext){
    userService.save(routingContext.getBodyAsJson(), ar -> {
      if(ar.succeeded()){
        LOGGER.info("User is created");
        routingContext.response().setStatusCode(201).end();
      } else {
        LOGGER.error("User not created");
        routingContext.response().setStatusCode(400).end();
      }
    });
  }

  public Router getRouter() {
    return router;
  }

}
