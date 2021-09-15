package com.example.frapizza.service.impl;

import com.example.frapizza.dao.OrderDao;
import com.example.frapizza.dao.PizzeriaDao;
import com.example.frapizza.entity.Delivery;
import com.example.frapizza.entity.Pizzeria;
import com.example.frapizza.service.OrderService;
import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {
  private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class.getName());
  private final PizzeriaDao pizzeriaDao;
  private final OrderDao orderDao;
  private final WebClient webClient;
  private final JsonObject props;
  private JsonObject userLocation;

  public OrderServiceImpl(Vertx vertx) {
    this.pizzeriaDao = PizzeriaDao.createProxy(vertx, PizzeriaDao.ADDRESS);
    this.orderDao = OrderDao.createProxy(vertx, OrderDao.ADDRESS);
    this.webClient = WebClient.create(vertx);
    this.props = vertx.getOrCreateContext().config();
  }

  @Override
  public void save(JsonObject deliveryPizzas, Handler<AsyncResult<Void>> resultHandler) {
    List<Integer> pizzaIds = deliveryPizzas.getJsonArray("pizzas")
      .stream()
      .map(e -> (Integer) e)
      .collect(Collectors.toList());

    Delivery delivery = new Delivery(deliveryPizzas.getJsonObject("delivery"));
    geocodeUserLocation(delivery)
      .compose(this::matrixRequestBody)
      .compose(this::fetchDistanceMatrix)
      .onSuccess(distanceMatrix -> {
        JsonObject distAndDuration = getMinDistanceAndDuration(distanceMatrix);
        delivery.setPizzeriaFrom(distAndDuration.getInteger("pizzeriaFrom"));
        delivery.setDistanceM(distAndDuration.getInteger("distanceM"));
        delivery.setExpTime(distAndDuration.getInteger("expTime"));
        orderDao.save(delivery, pizzaIds, resultHandler);
      })
      .onFailure(ex -> resultHandler.handle(Future.failedFuture(ex)));
  }

  @Override
  public void delete(Long id, Handler<AsyncResult<Void>> resultHandler) {
    orderDao.delete(id, resultHandler);
  }

  @Override
  public void readByCurrentUser(Long userId, Handler<AsyncResult<JsonArray>> resultHandler) {
    orderDao.readByCurrentUser(userId, resultHandler);
  }

  @Override
  public void readAll(Handler<AsyncResult<JsonArray>> resultHandler) {
    orderDao.readAll(resultHandler);
  }

  private Future<Void> geocodeUserLocation(Delivery delivery) {
    String requestUrl = "nominatim.openstreetmap.org";
    String qParam = delivery.getCity() + " "
      + delivery.getStreet() + " "
      + delivery.getBuilding();
    return Future.future(promise -> httpGetGeocode(requestUrl, qParam, promise));
  }

  private void httpGetGeocode(String requestUrl, String qParam, Promise<Void> promise) {
    webClient
      .get(80, requestUrl, "/")
      .addQueryParam("q", qParam)
      .addQueryParam("format", "json")
      .addQueryParam("limit", "1")
      .expect(ResponsePredicate.SC_SUCCESS)
      .send()
      .map(HttpResponse::bodyAsJsonArray)
      .map(jsonArray -> jsonArray.getJsonObject(0))
      .map(json -> userLocation = json)
      .onSuccess(response -> {
        LOGGER.info("http get succeeded: " + response);
        promise.complete();
      })
      .onFailure(ex -> {
        LOGGER.error("http get failed: " + requestUrl + " " + qParam);
        promise.fail(ex);
      });
  }

  private JsonObject getMinDistanceAndDuration(JsonObject distanceMatrix) {
    JsonArray durations = distanceMatrix.getJsonArray("durations");
    JsonArray distances = distanceMatrix.getJsonArray("distances");
    int minDistIndex = getMinDistanceIndex(distances);
    JsonObject jsonResult = new JsonObject();
    jsonResult.put("pizzeriaFrom", minDistIndex);
    jsonResult.put("distanceM", distances.getJsonArray(minDistIndex).getInteger(0));
    jsonResult.put("expTime", durations.getJsonArray(minDistIndex).getInteger(0));
    return jsonResult;
  }

  private int getMinDistanceIndex(JsonArray distances) {
    int minDistIndex = 0;
    Integer minDist = distances.getJsonArray(0).getInteger(0);
    for (int i = 0, size = distances.size(); i < size; i++) {
      Integer dist = distances.getJsonArray(i).getInteger(0);
      if (dist < minDist) {
        minDistIndex = i;
        minDist = dist;
      }
    }
    return minDistIndex;
  }

  private Future<JsonObject> fetchDistanceMatrix(JsonObject matrixRequestBody) {
    return webClient
      .post(80, "api.openrouteservice.org", "/v2/matrix/driving-car")
      .putHeader("Authorization", props.getString("OPENROUTE_TOKEN"))
      .expect(ResponsePredicate.SC_SUCCESS)
      .sendJsonObject(matrixRequestBody)
      .map(HttpResponse::bodyAsJsonObject);
  }

  private Future<JsonObject> matrixRequestBody(Void unused) {
    JsonObject requestBody = new JsonObject();
    requestBody.put("metrics", new JsonArray()
      .add("distance")
      .add("duration"));
    requestBody.put("units", "m");
    Promise<JsonObject> promise = Promise.promise();
    getAllPizzerias()
      .onSuccess(pizzerias -> {
        JsonObject locations = locationsJson(pizzerias);
        requestBody.mergeIn(locations);
        promise.complete(requestBody);
      })
      .onFailure(promise::fail);
    return promise.future();
  }

  private JsonObject locationsJson(List<Pizzeria> pizzerias) {
    JsonArray locations = new JsonArray();
    JsonArray destinationIndexes = new JsonArray();
    JsonArray sourceIndexes = new JsonArray();
    for (int i = 0, size = pizzerias.size(); i < size; i++) {
      sourceIndexes.add(i);
      Pizzeria p = pizzerias.get(i);
      locations.add(new JsonArray()
        .add(p.getLongitude())
        .add(p.getLatitude()));
    }
    locations.add(new JsonArray()
      .add(userLocation.getValue("lon"))
      .add(userLocation.getValue("lat")));
    destinationIndexes.add(pizzerias.size());
    return new JsonObject()
      .put("locations", locations)
      .put("sources", sourceIndexes)
      .put("destinations", destinationIndexes);

  }

  private Future<List<Pizzeria>> getAllPizzerias() {
    Promise<List<Pizzeria>> promise = Promise.promise();
    pizzeriaDao.readAll(ar -> {
      if (ar.succeeded()) {
        List<Pizzeria> pizzerias = new ArrayList<>();
        JsonArray jsonArray = ar.result();
        for (Object obj : jsonArray) {
          JsonObject jsonObject = (JsonObject) obj;
          pizzerias.add(new Pizzeria(jsonObject));
        }
        promise.complete(pizzerias);
      } else {
        promise.fail(ar.cause());
      }
    });
    return promise.future();
  }
}
