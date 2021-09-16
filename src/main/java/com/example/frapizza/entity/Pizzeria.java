package com.example.frapizza.entity;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

@DataObject(generateConverter = true)
public class Pizzeria {
  private Integer id;
  private String city;
  private String street;
  private String building;
  private Float latitude;
  private Float longitude;

  public Pizzeria(JsonObject json) {
    this.id = json.getInteger("id");
    this.city = json.getString("city");
    this.street = json.getString("street");
    this.building = json.getString("building");
    if(json.getFloat("latitude") != null){
      this.latitude = json.getFloat("latitude");
    }
    if(json.getFloat("longitude") != null){
      this.longitude = json.getFloat("longitude");
    }
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getBuilding() {
    return building;
  }

  public void setBuilding(String building) {
    this.building = building;
  }

  public Float getLatitude() {
    return latitude;
  }

  public void setLatitude(Float latitude) {
    this.latitude = latitude;
  }

  public Float getLongitude() {
    return longitude;
  }

  public void setLongitude(Float longitude) {
    this.longitude = longitude;
  }

  public JsonObject toJson() {
    return JsonObject.mapFrom(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Pizzeria pizzeria = (Pizzeria) o;
    return Objects.equals(city, pizzeria.city) && Objects.equals(street, pizzeria.street) && Objects.equals(building, pizzeria.building) && latitude.equals(pizzeria.latitude) && longitude.equals(pizzeria.longitude);
  }

  @Override
  public int hashCode() {
    return Objects.hash(city, street, building, latitude, longitude);
  }
}
