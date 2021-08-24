/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.frapizza.rxjava3.service;

import io.vertx.rxjava3.RxHelper;
import io.vertx.rxjava3.ObservableHelper;
import io.vertx.rxjava3.FlowableHelper;
import io.vertx.rxjava3.impl.AsyncResultMaybe;
import io.vertx.rxjava3.impl.AsyncResultSingle;
import io.vertx.rxjava3.impl.AsyncResultCompletable;
import io.vertx.rxjava3.WriteStreamObserver;
import io.vertx.rxjava3.WriteStreamSubscriber;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Collectors;
import io.vertx.core.Handler;
import io.vertx.core.AsyncResult;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.lang.rx.RxGen;
import io.vertx.lang.rx.TypeArg;
import io.vertx.lang.rx.MappingIterator;


@RxGen(com.example.frapizza.service.AuthService.class)
public class AuthService {

  @Override
  public String toString() {
    return delegate.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AuthService that = (AuthService) o;
    return delegate.equals(that.delegate);
  }
  
  @Override
  public int hashCode() {
    return delegate.hashCode();
  }

  public static final TypeArg<AuthService> __TYPE_ARG = new TypeArg<>(    obj -> new AuthService((com.example.frapizza.service.AuthService) obj),
    AuthService::getDelegate
  );

  private final com.example.frapizza.service.AuthService delegate;
  
  public AuthService(com.example.frapizza.service.AuthService delegate) {
    this.delegate = delegate;
  }

  public AuthService(Object delegate) {
    this.delegate = (com.example.frapizza.service.AuthService)delegate;
  }

  public com.example.frapizza.service.AuthService getDelegate() {
    return delegate;
  }

  public static com.example.frapizza.rxjava3.service.AuthService create(io.vertx.rxjava3.core.Vertx vertx, io.vertx.rxjava3.pgclient.PgPool pool) { 
    com.example.frapizza.rxjava3.service.AuthService ret = com.example.frapizza.rxjava3.service.AuthService.newInstance((com.example.frapizza.service.AuthService)com.example.frapizza.service.AuthService.create(vertx.getDelegate(), pool.getDelegate()));
    return ret;
  }

  public static com.example.frapizza.rxjava3.service.AuthService createProxy(io.vertx.rxjava3.core.Vertx vertx, java.lang.String address) { 
    com.example.frapizza.rxjava3.service.AuthService ret = com.example.frapizza.rxjava3.service.AuthService.newInstance((com.example.frapizza.service.AuthService)com.example.frapizza.service.AuthService.createProxy(vertx.getDelegate(), address));
    return ret;
  }

  public io.reactivex.rxjava3.core.Single<io.vertx.core.json.JsonObject> authentication(io.vertx.core.json.JsonObject credentials) { 
    io.reactivex.rxjava3.core.Single<io.vertx.core.json.JsonObject> ret = rxAuthentication(credentials);
    ret = ret.cache();
    ret.subscribe(io.vertx.rxjava3.SingleHelper.nullObserver());
    return ret;
  }

  public io.reactivex.rxjava3.core.Single<io.vertx.core.json.JsonObject> rxAuthentication(io.vertx.core.json.JsonObject credentials) { 
    return AsyncResultSingle.toSingle( resultHandler -> {
      delegate.authentication(credentials, resultHandler);
    });
  }

  public static final java.lang.String ADDRESS = com.example.frapizza.service.AuthService.ADDRESS;
  public static AuthService newInstance(com.example.frapizza.service.AuthService arg) {
    return arg != null ? new AuthService(arg) : null;
  }

}
