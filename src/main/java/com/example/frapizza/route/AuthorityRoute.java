package com.example.frapizza.route;

import com.example.frapizza.service.PizzaService;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorityRoute implements AuthorityRouter {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthorityRoute.class.getName());
  private final Router router;
  private final PizzaService pizzaService;

  public AuthorityRoute(Vertx vertx) {
    pizzaService = PizzaService.createProxy(vertx, PizzaService.ADDRESS);
    this.router = Router.router(vertx);
    router.get("/:id/pizzas")
      .handler(this::readAuthorityPizzas);
  }

  @Override
  public void readAuthorityPizzas(RoutingContext routingContext) {
    String authorityStr = routingContext.pathParam("id");
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

  @Override
  public Router getRouter() {
    return router;
  }
}
