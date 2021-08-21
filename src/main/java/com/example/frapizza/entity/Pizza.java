package com.example.frapizza.entity;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "pizzas")
public class Pizza {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User createdBy;

  @Column(name = "created_at")
  private OffsetDateTime createdAt;

  @ManyToMany
  @JoinTable(
    name = "pizza_ingredients",
    joinColumns = @JoinColumn(name = "pizza_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "ingredient_id", referencedColumnName = "id"))
  private Set<Ingredient> ingredients;

  @OneToMany(mappedBy = "pizza")
  private List<Order> orders;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Set<Ingredient> getIngredients() {
    return ingredients;
  }

  public void setIngredients(Set<Ingredient> ingredients) {
    this.ingredients = ingredients;
  }

  public void addIngredient(Ingredient ingredient){
    ingredients.add(ingredient);
    ingredient.getPizzas().add(this);
  }

  public List<Order> getOrders() {
    return orders;
  }

  public void setOrders(List<Order> orders) {
    this.orders = orders;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Pizza pizza = (Pizza) o;
    return id.equals(pizza.id) && name.equals(pizza.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
