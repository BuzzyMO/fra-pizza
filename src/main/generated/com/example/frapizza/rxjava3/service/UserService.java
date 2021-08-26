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


@RxGen(com.example.frapizza.service.UserService.class)
public class UserService {

  @Override
  public String toString() {
    return delegate.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserService that = (UserService) o;
    return delegate.equals(that.delegate);
  }
  
  @Override
  public int hashCode() {
    return delegate.hashCode();
  }

  public static final TypeArg<UserService> __TYPE_ARG = new TypeArg<>(    obj -> new UserService((com.example.frapizza.service.UserService) obj),
    UserService::getDelegate
  );

  private final com.example.frapizza.service.UserService delegate;
  
  public UserService(com.example.frapizza.service.UserService delegate) {
    this.delegate = delegate;
  }

  public UserService(Object delegate) {
    this.delegate = (com.example.frapizza.service.UserService)delegate;
  }

  public com.example.frapizza.service.UserService getDelegate() {
    return delegate;
  }

  public static com.example.frapizza.rxjava3.service.UserService create(io.vertx.rxjava3.core.Vertx vertx) { 
    com.example.frapizza.rxjava3.service.UserService ret = com.example.frapizza.rxjava3.service.UserService.newInstance((com.example.frapizza.service.UserService)com.example.frapizza.service.UserService.create(vertx.getDelegate()));
    return ret;
  }

  public static com.example.frapizza.rxjava3.service.UserService createProxy(io.vertx.rxjava3.core.Vertx vertx, java.lang.String address) { 
    com.example.frapizza.rxjava3.service.UserService ret = com.example.frapizza.rxjava3.service.UserService.newInstance((com.example.frapizza.service.UserService)com.example.frapizza.service.UserService.createProxy(vertx.getDelegate(), address));
    return ret;
  }

  public io.reactivex.rxjava3.core.Completable save(io.vertx.core.json.JsonObject userJson) { 
    io.reactivex.rxjava3.core.Completable ret = rxSave(userJson);
    ret = ret.cache();
    ret.subscribe(io.vertx.rxjava3.CompletableHelper.nullObserver());
    return ret;
  }

  public io.reactivex.rxjava3.core.Completable rxSave(io.vertx.core.json.JsonObject userJson) { 
    return AsyncResultCompletable.toCompletable( resultHandler -> {
      delegate.save(userJson, resultHandler);
    });
  }

  public io.reactivex.rxjava3.core.Single<io.vertx.core.json.JsonObject> update(io.vertx.core.json.JsonObject userJson) { 
    io.reactivex.rxjava3.core.Single<io.vertx.core.json.JsonObject> ret = rxUpdate(userJson);
    ret = ret.cache();
    ret.subscribe(io.vertx.rxjava3.SingleHelper.nullObserver());
    return ret;
  }

  public io.reactivex.rxjava3.core.Single<io.vertx.core.json.JsonObject> rxUpdate(io.vertx.core.json.JsonObject userJson) { 
    return AsyncResultSingle.toSingle( resultHandler -> {
      delegate.update(userJson, resultHandler);
    });
  }

  public io.reactivex.rxjava3.core.Single<io.vertx.core.json.JsonObject> delete(java.lang.String id) { 
    io.reactivex.rxjava3.core.Single<io.vertx.core.json.JsonObject> ret = rxDelete(id);
    ret = ret.cache();
    ret.subscribe(io.vertx.rxjava3.SingleHelper.nullObserver());
    return ret;
  }

  public io.reactivex.rxjava3.core.Single<io.vertx.core.json.JsonObject> rxDelete(java.lang.String id) { 
    return AsyncResultSingle.toSingle( resultHandler -> {
      delegate.delete(id, resultHandler);
    });
  }

  public io.reactivex.rxjava3.core.Single<io.vertx.core.json.JsonObject> readById(java.lang.String id) { 
    io.reactivex.rxjava3.core.Single<io.vertx.core.json.JsonObject> ret = rxReadById(id);
    ret = ret.cache();
    ret.subscribe(io.vertx.rxjava3.SingleHelper.nullObserver());
    return ret;
  }

  public io.reactivex.rxjava3.core.Single<io.vertx.core.json.JsonObject> rxReadById(java.lang.String id) { 
    return AsyncResultSingle.toSingle( resultHandler -> {
      delegate.readById(id, resultHandler);
    });
  }

  public io.reactivex.rxjava3.core.Single<io.vertx.core.json.JsonObject> readAll() { 
    io.reactivex.rxjava3.core.Single<io.vertx.core.json.JsonObject> ret = rxReadAll();
    ret = ret.cache();
    ret.subscribe(io.vertx.rxjava3.SingleHelper.nullObserver());
    return ret;
  }

  public io.reactivex.rxjava3.core.Single<io.vertx.core.json.JsonObject> rxReadAll() { 
    return AsyncResultSingle.toSingle( resultHandler -> {
      delegate.readAll(resultHandler);
    });
  }

  public static final java.lang.String ADDRESS = com.example.frapizza.service.UserService.ADDRESS;
  public static UserService newInstance(com.example.frapizza.service.UserService arg) {
    return arg != null ? new UserService(arg) : null;
  }

}
