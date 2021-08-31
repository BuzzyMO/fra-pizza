package com.example.frapizza.route;

import com.example.frapizza.service.PizzeriaService;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PizzeriaRoute implements PizzeriaRouter{
  private static final Logger LOGGER = LoggerFactory.getLogger(PizzeriaRoute.class.getName());
  private final Router router;
  private final PizzeriaService pizzeriaService;

  public PizzeriaRoute(Vertx vertx) {
    this.pizzeriaService = PizzeriaService.createProxy(vertx, PizzeriaService.ADDRESS);
    this.router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.post("/save").handler(this::save);
    router.put("/update").handler(this::update);
    router.delete("/delete/:id").handler(this::delete);
    router.get("/read").handler(this::readAll);
  }

  private void save(RoutingContext routingContext) {
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

  private void update(RoutingContext routingContext){
    pizzeriaService.update(routingContext.getBodyAsJson(), ar -> {
      if (ar.succeeded()) {
        LOGGER.info("Pizzeria is updated");
        routingContext.response().setStatusCode(201).end();
      } else {
        LOGGER.error("Pizzeria not updated: " + ar.cause().getMessage());
        routingContext.response().setStatusCode(400).end();
      }
    });
  }

  private void delete(RoutingContext routingContext){
    String idStr = routingContext.pathParam("id");
    Integer id = Integer.parseInt(idStr);
    pizzeriaService.delete(id, ar -> {
      if (ar.succeeded()) {
        LOGGER.info("Pizzeria is deleted");
        routingContext.response().setStatusCode(201).end();
      } else {
        LOGGER.error("Pizzeria not deleted: " + ar.cause().getMessage());
        routingContext.response().setStatusCode(400).end();
      }
    });
  }

  private void readAll(RoutingContext routingContext){
    pizzeriaService.readAll(ar -> {
      if (ar.succeeded()) {
        LOGGER.info("Pizzeria is read");
        routingContext.response().setStatusCode(201).end(ar.result().toBuffer());
      } else {
        LOGGER.error("Pizzeria not read: " + ar.cause().getMessage());
        routingContext.response().setStatusCode(400).end();
      }
    });
  }
  @Override
  public Router getRouter() {
    return router;
  }
}
