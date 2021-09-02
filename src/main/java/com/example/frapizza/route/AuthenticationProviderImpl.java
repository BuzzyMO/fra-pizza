package com.example.frapizza.route;

import com.example.frapizza.service.AuthService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.auth.authorization.RoleBasedAuthorization;

public class AuthenticationProviderImpl implements AuthenticationProvider {
  private final AuthService authService;

  public AuthenticationProviderImpl(Vertx vertx) {
    this.authService = AuthService.createProxy(vertx, AuthService.ADDRESS);
  }

  @Override
  public void authenticate(JsonObject credentials, Handler<AsyncResult<User>> resultHandler) {
    authService.authentication(credentials, ar -> {
      if (ar.succeeded()) {
        JsonObject json = ar.result();
        JsonArray roles = json.getJsonArray("roles");
        User user = User.create(json);
        for (int i = 0, size = roles.size(); i < size; i++) {
          String roleValue = roles.getJsonObject(i).getString("value");
          user.authorizations().add(roleValue, RoleBasedAuthorization.create(roleValue));
        }
        resultHandler.handle(Future.succeededFuture(user));
      } else {
        resultHandler.handle(Future.failedFuture(ar.cause()));
      }
    });
  }
}
