package com.example.frapizza.verticle;

import com.example.frapizza.route.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.CookieSameSite;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.SessionStore;
import io.vertx.ext.web.sstore.redis.RedisSessionStore;
import io.vertx.redis.client.Redis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpVerticle extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(HttpVerticle.class.getName());
  private static final int PORT = 8080;

  @Override
  public void start(Promise<Void> promise) {
    SessionStore sessionStore = getRedisStore();
    SessionHandler sessionHandler = getSessionHandler(sessionStore);
    CorsHandler corsHandler = corsConfig();

    Router router = Router.router(vertx);
    router.route()
      .handler(corsHandler);

    mountSubRouters(router, sessionHandler, sessionStore);

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

  private RedisSessionStore getRedisStore(){
    Redis redis = Redis.createClient(vertx, config().getString("REDIS_URI"));
    return RedisSessionStore.create(vertx, redis);
  }

  private SessionHandler getSessionHandler(SessionStore store) {
    SessionHandler sessionHandler = SessionHandler.create(store);
    sessionHandler.setCookieSameSite(CookieSameSite.STRICT);
    return sessionHandler;
  }

  private CorsHandler corsConfig(){
    return CorsHandler.create()
      .addOrigin(config().getString("CORS_ORIGIN"))
      .allowCredentials(true).allowedMethod(HttpMethod.DELETE);
  }

  private void mountSubRouters(Router router, SessionHandler sessionHandler, SessionStore sessionStore){
    Router authRouter = AuthRouter.create(vertx, sessionHandler, sessionStore);
    Router userRouter = UserRouter.create(vertx);
    Router pizzaRouter = PizzaRouter.create(vertx);
    Router orderRouter = OrderRouter.create(vertx);
    Router ingredientRouter = IngredientRouter.create(vertx);
    Router pizzeriaRouter = PizzeriaRouter.create(vertx);
    Router authorityRouter = AuthorityRouter.create(vertx);
    router.mountSubRouter("/api/auth", authRouter);
    router.mountSubRouter("/api/users", userRouter);
    router.mountSubRouter("/api/pizzas", pizzaRouter);
    router.mountSubRouter("/api/orders", orderRouter);
    router.mountSubRouter("/api/ingredients", ingredientRouter);
    router.mountSubRouter("/api/pizzerias", pizzeriaRouter);
    router.mountSubRouter("/api/authorities", authorityRouter);
  }

}
