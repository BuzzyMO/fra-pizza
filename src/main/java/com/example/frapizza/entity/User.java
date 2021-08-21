package com.example.frapizza.entity;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NaturalId(mutable = true)
  @Column(nullable = false)
  private String email;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(nullable = false)
  private String password;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  @OneToMany(mappedBy = "createdBy")
  private List<Pizza> pizzas;

  @OneToMany(mappedBy = "user")
  private List<Delivery> deliveries;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public List<Pizza> getPizzas() {
    return pizzas;
  }

  public void setPizzas(List<Pizza> pizzas) {
    this.pizzas = pizzas;
  }

  public List<Delivery> getDeliveries() {
    return deliveries;
  }

  public void setDeliveries(List<Delivery> deliveries) {
    this.deliveries = deliveries;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return email.equals(user.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email);
  }
}
