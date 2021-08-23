package com.example.frapizza.verticle;

import com.example.frapizza.handler.UserHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.SessionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpVerticle extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(HttpVerticle.class.getName());
  private static final int PORT = 8080;

  @Override
  public void start(Promise<Void> promise) {
    Router router = Router.router(vertx);
    UserHandler userHandler = new UserHandler(vertx);
    router.mountSubRouter("/api/user", userHandler.getRouter());

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(PORT)
      .onSuccess(ok -> {
        LOGGER.info("Http server is started at port: " + PORT);
        promise.complete();
      })
      .onFailure(ex -> {
        LOGGER.info("Http server fail started: " + PORT + " - " + ex.getMessage());
        promise.fail(ex);
      });
  }
}
