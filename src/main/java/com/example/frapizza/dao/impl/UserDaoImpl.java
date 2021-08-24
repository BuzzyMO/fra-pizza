package com.example.frapizza.dao.impl;

import com.example.frapizza.dao.UserDao;
import com.example.frapizza.entity.User;
import io.vertx.core.Future;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDaoImpl implements UserDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class.getName());
  private final PgPool pool;

  public UserDaoImpl(PgPool pool) {
    this.pool = pool;
  }

  @Override
  public Future<Void> save(User entity) {
    String query = "INSERT INTO users(first_name, last_name, email, password, phone_number) " +
      "VALUES ($1, $2, $3, $4, $5)";
    return Future.future(promise -> pool.withTransaction(client -> client
        .preparedQuery(query)
        .execute(Tuple.of(entity.getFirstName(), entity.getLastName(), entity.getEmail(), entity.getPassword(), entity.getPhoneNumber()))
        .onSuccess(rs -> LOGGER.info("User is created: " + entity.getEmail()))
        .onFailure(ex -> LOGGER.error("User creation failed: " + ex.getMessage())))
      .onSuccess(rs -> {
        LOGGER.info("Transaction succeeded");
        promise.complete();
      })
      .onFailure(ex -> {
        LOGGER.error("Transaction failed");
        promise.fail(ex);
      }));
  }
}
