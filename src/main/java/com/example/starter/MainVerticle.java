package com.example.starter;

import com.example.dto.Article;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainVerticle extends AbstractVerticle {

  private Map readingList = new LinkedHashMap();

  private void createSomeDate() {
    Article article1 = new Article(
      "Forbes", "www.forbes.com"
    );
    readingList.put(article1.getId(), article1);

    Article article2 = new Article(
      "Adventurer", "www.adventurer.com"
    );
    readingList.put(article2.getId(), article2);

  }

  private void getAll(RoutingContext rc) {
    rc.response().putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(readingList.values()));
  }

  private void addOne(RoutingContext rc) {
    Article article = rc.getBodyAsJson().mapTo(Article.class);
    readingList.put(article.getId(), article);
    rc.response().setStatusCode(201).putHeader("content-type",
      "application/json; charset=utf-8")
      .end(Json.encodePrettily(article));

  }

  private void deleteOne(RoutingContext rc) {
    String id = rc.request().getParam("id");
    try {
      Integer idAsInteger = Integer.valueOf(id);
      readingList.remove(idAsInteger);
      rc.response().setStatusCode(204).end();
    } catch (NumberFormatException e) {
      rc.response().setStatusCode(400).end();
    }
  }


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    createSomeDate();
    // Router is responsible for delegating the requests to the right handler
    Router router = Router.router(vertx);
    router.route("/api/articles*").handler(BodyHandler.create());
    router.post("/api/articles").handler(this::addOne);
    router.get("/api/articles").handler(this::getAll);
    router.delete("/api/articles/:id").handler(this::deleteOne);
//    // routes define how the requests had to be handled
//    // handlers - actual actions processing your requests
//    router.route().handler(context -> {
//      String address = context.request().connection().remoteAddress().toString();
//      MultiMap queryParams = context.queryParams();
//      String name = queryParams.contains("name") ? queryParams.get("name") : "unknown";
//      context.json(
//        new JsonObject()
//          .put("name", name)
//          .put("address", address)
//          .put("message","Hello "+name+ " connected from "+ address)
//      );
//    });

    vertx.createHttpServer().requestHandler(router)
      .listen(8888)
      .onSuccess(server -> System.out.println("http server started on" + server.actualPort()));
  }
}
