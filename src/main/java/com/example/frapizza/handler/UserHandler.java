package com.example.frapizza.handler;

import com.example.frapizza.service.UserService;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class UserHandler {
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
        routingContext.response().setStatusCode(201).end();
      } else {
        routingContext.response().setStatusCode(400).end();
      }
    });
  }

  public Router getRouter() {
    return router;
  }

}
