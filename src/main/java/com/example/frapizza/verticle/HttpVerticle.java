package com.example.frapizza.verticle;

import com.example.frapizza.handler.AuthHandler;
import com.example.frapizza.handler.UserHandler;
import com.example.frapizza.util.ConfigLoader;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.redis.RedisSessionStore;
import io.vertx.redis.client.Command;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpVerticle extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(HttpVerticle.class.getName());
  private static final int PORT = 8080;

  @Override
  public void start(Promise<Void> promise) {
    JsonObject props = ConfigLoader.loadEnv(vertx);

    Redis redis = Redis.createClient(vertx, props.getString("REDIS_URI"));
    redis.send(Request.cmd(Command.PING))
      .onSuccess(r -> System.out.println("r = " + r))
      .onFailure(ex -> System.out.println("ex = " + ex));

    RedisSessionStore store = RedisSessionStore.create(vertx, redis);
    SessionHandler sessionHandler = SessionHandler.create(store);

    AuthenticationHandler authHandler = AuthHandler.createAuthHandler(vertx);

    Router router = Router.router(vertx);
    router.route().handler(sessionHandler);
    router.route().handler(authHandler);
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
