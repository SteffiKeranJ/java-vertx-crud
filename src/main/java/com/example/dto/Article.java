package com.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

//@Data

public class Article {
  private static final AtomicInteger COUNTER = new AtomicInteger();
  @JsonIgnore
  private final int id;
  private String name;
  private String url;

  public Article(String name, String url) {
    this.id = COUNTER.getAndIncrement();
    this.name = name;
    this.url = url;
  }

  public Article() {
    this.id = COUNTER.getAndIncrement();
  }

  public int getId() {
    return id;
  }


  public String getName() {
    return name;
  }

  public Article setName(String name) {
    this.name = name;
    return this;
  }

  public String getUrl() {
    return url;
  }

  public Article setUrl(String url) {
    this.url = url;
    return this;
  }
}
