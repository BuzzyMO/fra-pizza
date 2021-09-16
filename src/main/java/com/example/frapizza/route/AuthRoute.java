package com.example.frapizza.route;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.SessionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthRoute implements AuthRouter {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthRoute.class.getName());
  private final Router router;

  public AuthRoute(Vertx vertx, SessionHandler sessionHandler, SessionStore sessionStore) {
    AuthenticationHandler authHandler = AuthHandler.createAuthenticationHandler(vertx);
    this.router = Router.router(vertx);
    router.post()
      .handler(sessionHandler)
      .handler(authHandler)
      .handler(this::auth);
    router.delete()
      .handler(ctx -> logout(ctx, sessionStore));
  }

  @Override
  public void auth(RoutingContext routingContext) {
    routingContext
      .response()
      .setStatusCode(200)
      .end();
  }

  @Override
  public void logout(RoutingContext routingContext, SessionStore sessionStore) {
    routingContext.session().destroy();
    sessionStore
      .delete(routingContext.session().id())
      .onSuccess(res -> {
        LOGGER.warn("Session is destroyed: " + routingContext.session().id());
        routingContext.response().setStatusCode(200).end();
      })
      .onFailure(ex -> {
        LOGGER.error("Session not destroyed: " + routingContext.session().id());
        routingContext.response().setStatusCode(400).end();
      });
  }

  @Override
  public Router getRouter() {
    return router;
  }
}
