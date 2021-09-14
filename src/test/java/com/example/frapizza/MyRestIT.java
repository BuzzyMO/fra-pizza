package com.example.frapizza;

import com.example.frapizza.verticle.DataVerticle;
import com.example.frapizza.verticle.HttpVerticle;
import com.example.frapizza.verticle.ServiceVerticle;
import io.reactiverse.junit5.web.WebClientOptionsInject;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Base64;

import static io.reactiverse.junit5.web.TestRequest.*;

@Testcontainers
@ExtendWith(VertxExtension.class)
public class MyRestIT {
  private static String encodedCredentials;

  @Container
  public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(PostgreSQLContainer.IMAGE)
    .withExposedPorts(5432);

  @WebClientOptionsInject
  public WebClientOptions options = new WebClientOptions()
    .setDefaultHost("localhost")
    .setDefaultPort(8080);

  @BeforeAll
  static void set_up(Vertx vertx, VertxTestContext testContext) {
    String credentials = "admin@gmail.com:changeme";
    encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

    postgres.start();
    doConfig(vertx)
      .map(MyRestIT::putDatabaseProps)
      .map(config -> new DeploymentOptions().setConfig(config))
      .compose(opt -> deployOtherVerticles(vertx, opt))
      .onSuccess(ok -> testContext.completeNow())
      .onFailure(testContext::failNow);
  }

  private static Future<JsonObject> doConfig(Vertx vertx) {
    ConfigStoreOptions envStore = new ConfigStoreOptions()
      .setType("env")
      .setConfig(new JsonObject().put("raw-data", true));
    ConfigRetrieverOptions options = new ConfigRetrieverOptions()
      .addStore(envStore);
    ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
    return retriever.getConfig();
  }

  private static JsonObject putDatabaseProps(JsonObject config) {
    return config.mergeIn(
      new JsonObject()
        .put("DATASOURCE_PORT", postgres.getMappedPort(5432))
        .put("DATASOURCE_URL", postgres.getJdbcUrl())
        .put("DATASOURCE_USERNAME", postgres.getUsername())
        .put("DATASOURCE_PASSWORD", postgres.getPassword())
        .put("DATASOURCE_DBNAME", postgres.getDatabaseName())
    );
  }

  private static Future<Void> deployOtherVerticles(Vertx vertx, DeploymentOptions options) {
    Future<String> httpVerticle = vertx.deployVerticle(new HttpVerticle(), options);
    Future<String> serviceVerticle = vertx.deployVerticle(new ServiceVerticle());
    Future<String> databaseVerticle = vertx.deployVerticle(new DataVerticle(), options);

    return CompositeFuture.all(httpVerticle, serviceVerticle, databaseVerticle).mapEmpty();
  }

  @Test
  void auth(Vertx vertx, WebClient client, VertxTestContext testContext) {
    testRequest(client, HttpMethod.POST, "/api/auth")
      .with(requestHeader("Authorization", "Basic " + encodedCredentials))
      .expect(statusCode(200))
      .send(testContext);
  }

  @Test
  void user_save(Vertx vertx, WebClient client, VertxTestContext testContext) {
    JsonObject testUser = new JsonObject()
      .put("firstName", "testFirst")
      .put("lastName", "testLast")
      .put("email", "test@gmail.com")
      .put("password", "pass123")
      .put("phoneNumber", "+3800000000");

    testRequest(client, HttpMethod.POST, "/api/users")
      .expect(statusCode(201))
      .sendJson(testUser, testContext);
  }

  @Test
  void user_get_all(Vertx vertx, WebClient client, VertxTestContext testContext) {
    testRequest(client, HttpMethod.GET, "/api/users")
      .with(requestHeader("Authorization", "Basic " + encodedCredentials))
      .expect(
        statusCode(200),
        responseHeader("Content-Type", "application/json"))
      .send(testContext)
      .map(HttpResponse::bodyAsJsonArray)
      .onSuccess(jsonArray -> Assertions.assertTrue(jsonArray.size() > 0));
  }

  @Test
  void user_update(Vertx vertx, WebClient client, VertxTestContext testContext) {
    JsonObject updatedUser = new JsonObject()
      .put("firstName", "updatedFirst")
      .put("lastName", "Pupkin")
      .put("email", "updated@gmail.com")
      .put("password", "testpass")
      .put("phoneNumber", "+38011111111");

    testRequest(client, HttpMethod.PUT, "/api/users/2")
      .with(
        requestHeader("Authorization", "Basic " + encodedCredentials),
        requestHeader("Content-Type", "application/json"))
      .expect(statusCode(200))
      .sendJson(updatedUser, testContext);
  }

  @Test
  void user_delete(Vertx vertx, WebClient client, VertxTestContext testContext) {
    testRequest(client, HttpMethod.DELETE, "/api/users/3")
      .with(requestHeader("Authorization", "Basic " + encodedCredentials))
      .expect(statusCode(204))
      .send(testContext);
  }

  @Test
  void pizzeria_save(Vertx vertx, WebClient client, VertxTestContext testContext) {
    JsonObject testPizzeria = new JsonObject()
      .put("city", "testCity")
      .put("street", "testStreet")
      .put("building", "42A")
      .put("latitude", 49.78113)
      .put("longitude", 36.44486);

    testRequest(client, HttpMethod.POST, "/api/pizzerias")
      .with(requestHeader("Authorization", "Basic " + encodedCredentials))
      .expect(statusCode(201))
      .sendJson(testPizzeria, testContext);
  }

  @Test
  void pizzeria_get_all(Vertx vertx, WebClient client, VertxTestContext testContext) {
    testRequest(client, HttpMethod.GET, "/api/pizzerias")
      .with(requestHeader("Authorization", "Basic " + encodedCredentials))
      .expect(
        statusCode(200),
        responseHeader("Content-Type", "application/json"))
      .send(testContext)
      .map(HttpResponse::bodyAsJsonArray)
      .onSuccess(jsonArray -> Assertions.assertTrue(jsonArray.size() > 0));
  }

  @Test
  void pizzeria_update(Vertx vertx, WebClient client, VertxTestContext testContext) {
    JsonObject updatedPizzeria = new JsonObject()
      .put("city", "Харьков")
      .put("street", "updatedStreet")
      .put("building", "42B")
      .put("latitude", 50.03501)
      .put("longitude", 36.21994);

    testRequest(client, HttpMethod.PUT, "/api/pizzerias/2")
      .with(
        requestHeader("Authorization", "Basic " + encodedCredentials),
        requestHeader("Content-Type", "application/json"))
      .expect(statusCode(200))
      .sendJson(updatedPizzeria, testContext);
  }

  @Test
  void pizzeria_delete(Vertx vertx, WebClient client, VertxTestContext testContext) {
    testRequest(client, HttpMethod.DELETE, "/api/pizzerias/3")
      .with(requestHeader("Authorization", "Basic " + encodedCredentials))
      .expect(statusCode(204))
      .send(testContext);
  }

  @Test
  void ingredient_save(Vertx vertx, WebClient client, VertxTestContext testContext) {
    JsonObject testIngredient = new JsonObject()
      .put("name", "testIngr")
      .put("cost", 3);

    testRequest(client, HttpMethod.POST, "/api/ingredients")
      .with(requestHeader("Authorization", "Basic " + encodedCredentials))
      .expect(statusCode(201))
      .sendJson(testIngredient, testContext);
  }

  @Test
  void ingredient_get_all(Vertx vertx, WebClient client, VertxTestContext testContext) {
    testRequest(client, HttpMethod.GET, "/api/ingredients")
      .with(requestHeader("Authorization", "Basic " + encodedCredentials))
      .expect(
        statusCode(200),
        responseHeader("Content-Type", "application/json"))
      .send(testContext)
      .map(HttpResponse::bodyAsJsonArray)
      .onSuccess(jsonArray -> Assertions.assertTrue(jsonArray.size() > 0));
  }

  @Test
  void ingredient_update(Vertx vertx, WebClient client, VertxTestContext testContext) {
    JsonObject updatedIngredient = new JsonObject()
      .put("name", "updatedIngr")
      .put("cost", 1);

    testRequest(client, HttpMethod.PUT, "/api/ingredients/3")
      .with(
        requestHeader("Authorization", "Basic " + encodedCredentials),
        requestHeader("Content-Type", "application/json"))
      .expect(statusCode(200))
      .sendJson(updatedIngredient, testContext);
  }

  @Test
  void ingredient_delete(Vertx vertx, WebClient client, VertxTestContext testContext) {
    testRequest(client, HttpMethod.DELETE, "/api/ingredients/4")
      .with(requestHeader("Authorization", "Basic " + encodedCredentials))
      .expect(statusCode(204))
      .send(testContext);
  }

  @Test
  void order_save(Vertx vertx, WebClient client, VertxTestContext testContext) {
    JsonObject testOrder = new JsonObject()
      .put("delivery", new JsonObject()
        .put("city", "Харьков")
        .put("street", "Целиноградская")
        .put("building", "30"))
      .put("pizzas", new JsonArray().add(1).add(2));

    testRequest(client, HttpMethod.POST, "/api/orders")
      .with(requestHeader("Authorization", "Basic " + encodedCredentials))
      .expect(statusCode(201))
      .sendJson(testOrder, testContext);
  }

  @Test
  void order_get_all(Vertx vertx, WebClient client, VertxTestContext testContext) {
    testRequest(client, HttpMethod.GET, "/api/orders")
      .with(requestHeader("Authorization", "Basic " + encodedCredentials))
      .expect(
        statusCode(200),
        responseHeader("Content-Type", "application/json"))
      .send(testContext)
      .map(HttpResponse::bodyAsJsonArray)
      .onSuccess(jsonArray -> Assertions.assertTrue(jsonArray.size() > 0));
  }

  @Test
  void order_delete(Vertx vertx, WebClient client, VertxTestContext testContext) {
    testRequest(client, HttpMethod.DELETE, "/api/orders/2")
      .with(requestHeader("Authorization", "Basic " + encodedCredentials))
      .expect(statusCode(204))
      .send(testContext);
  }


  @Test
  void pizza_save(Vertx vertx, WebClient client, VertxTestContext testContext) {
    JsonObject testPizza = new JsonObject()
      .put("pizza", new JsonObject().put("name", "testPizza"))
      .put("ingredients", new JsonArray().add(1).add(2));

    testRequest(client, HttpMethod.POST, "/api/pizzas")
      .with(requestHeader("Authorization", "Basic " + encodedCredentials))
      .expect(statusCode(201))
      .sendJson(testPizza, testContext);
  }

  @Test
  void pizza_get_by_authority(Vertx vertx, WebClient client, VertxTestContext testContext) {
    testRequest(client, HttpMethod.GET, "/api/pizzas/authority/1")
      .expect(
        statusCode(200),
        responseHeader("Content-Type", "application/json"))
      .send(testContext)
      .map(HttpResponse::bodyAsJsonArray)
      .onSuccess(jsonArray -> Assertions.assertTrue(jsonArray.size() > 0));
  }

  @Test
  void pizza_get_by_user(Vertx vertx, WebClient client, VertxTestContext testContext) {
    testRequest(client, HttpMethod.GET, "/api/pizzas/user")
      .with(requestHeader("Authorization", "Basic " + encodedCredentials))
      .expect(
        statusCode(200),
        responseHeader("Content-Type", "application/json"))
      .send(testContext)
      .map(HttpResponse::bodyAsJsonArray)
      .onSuccess(jsonArray -> Assertions.assertTrue(jsonArray.size() > 0));
  }

  @Test
  void pizza_get_all(Vertx vertx, WebClient client, VertxTestContext testContext) {
    testRequest(client, HttpMethod.GET, "/api/pizzas")
      .with(requestHeader("Authorization", "Basic " + encodedCredentials))
      .expect(
        statusCode(200),
        responseHeader("Content-Type", "application/json"))
      .send(testContext)
      .map(HttpResponse::bodyAsJsonArray)
      .onSuccess(jsonArray -> Assertions.assertTrue(jsonArray.size() > 0));
  }

  @Test
  void pizza_update(Vertx vertx, WebClient client, VertxTestContext testContext) {
    JsonObject updatedPizza = new JsonObject()
      .put("name", "updatedPizza")
      .put("createdBy", 2);

    testRequest(client, HttpMethod.PUT, "/api/pizzas/2")
      .with(
        requestHeader("Authorization", "Basic " + encodedCredentials),
        requestHeader("Content-Type", "application/json"))
      .expect(statusCode(200))
      .sendJson(updatedPizza, testContext);
  }

  @Test
  void pizza_delete(Vertx vertx, WebClient client, VertxTestContext testContext) {
    testRequest(client, HttpMethod.DELETE, "/api/pizzas/3")
      .with(requestHeader("Authorization", "Basic " + encodedCredentials))
      .expect(statusCode(204))
      .send(testContext);
  }

  @AfterAll
  static void stop() {
    postgres.stop();
    System.out.println("postgres = " + postgres.isRunning());
  }

}
