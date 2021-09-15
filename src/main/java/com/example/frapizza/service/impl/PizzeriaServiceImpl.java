package com.example.frapizza.service.impl;

import com.example.frapizza.dao.PizzeriaDao;
import com.example.frapizza.entity.Pizzeria;
import com.example.frapizza.service.PizzeriaService;
import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PizzeriaServiceImpl implements PizzeriaService {
  private static final Logger LOGGER = LoggerFactory.getLogger(PizzeriaServiceImpl.class.getName());
  private final PizzeriaDao pizzeriaDao;
  private final WebClient webClient;

  public PizzeriaServiceImpl(Vertx vertx) {
    this.pizzeriaDao = PizzeriaDao.createProxy(vertx, PizzeriaDao.ADDRESS);
    this.webClient = WebClient.create(vertx);
  }

  @Override
  public void save(JsonObject pizzeriaJson, Handler<AsyncResult<Void>> resultHandler) {
    Pizzeria pizzeria = new Pizzeria(pizzeriaJson);
    geocodePizzeriaLocation(pizzeria)
      .onSuccess(geocode -> {
        pizzeria.setLongitude(Float.parseFloat(geocode.getString("lon")));
        pizzeria.setLatitude(Float.parseFloat(geocode.getString("lat")));
        pizzeriaDao.save(pizzeria, resultHandler);
      })
      .onFailure(ex -> resultHandler.handle(Future.failedFuture(ex)));
  }

  @Override
  public void update(Integer id, JsonObject pizzeriaJson, Handler<AsyncResult<Void>> resultHandler) {
    Pizzeria pizzeria = new Pizzeria(pizzeriaJson);
    pizzeriaDao.update(id, pizzeria, resultHandler);
  }

  @Override
  public void delete(Integer id, Handler<AsyncResult<Void>> resultHandler) {
    pizzeriaDao.delete(id, resultHandler);
  }

  @Override
  public void readAll(Handler<AsyncResult<JsonArray>> resultHandler) {
    pizzeriaDao.readAll(resultHandler);
  }


  private Future<JsonObject> geocodePizzeriaLocation(Pizzeria pizzeria) {
    String requestUrl = "nominatim.openstreetmap.org";
    String qParam = pizzeria.getCity() + " "
      + pizzeria.getStreet() + " "
      + pizzeria.getBuilding();
    return Future.future(promise -> httpGetGeocode(requestUrl, qParam, promise));
  }

  private void httpGetGeocode(String requestUrl, String qParam, Promise<JsonObject> promise) {
    webClient
      .get(80, requestUrl, "/")
      .addQueryParam("q", qParam)
      .addQueryParam("format", "json")
      .addQueryParam("limit", "1")
      .expect(ResponsePredicate.SC_SUCCESS)
      .send()
      .map(HttpResponse::bodyAsJsonArray)
      .map(jsonArray -> jsonArray.getJsonObject(0))
      .onSuccess(response -> {
        LOGGER.info("http get succeeded: " + response);
        promise.complete(response);
      })
      .onFailure(ex -> {
        LOGGER.error("http get failed: " + requestUrl + " " + qParam);
        promise.fail(ex);
      });
  }
}
