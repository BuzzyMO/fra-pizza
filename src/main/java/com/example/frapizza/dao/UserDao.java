package com.example.frapizza.dao;

import com.example.frapizza.entity.User;
import io.vertx.core.Future;

public interface UserDao {
  Future<Void> save(User entity);
}
