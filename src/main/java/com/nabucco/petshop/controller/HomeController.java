package com.nabucco.petshop.controller;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.restexpress.Request;
import org.restexpress.Response;

public class HomeController {

  public String hello(Request request, Response response) {
    response.setResponseStatus(HttpResponseStatus.OK);
    return "Nabucco' Petshop Log Analytics Service";
  }

  public void health(Request request, Response response) {
    response.setResponseStatus(HttpResponseStatus.OK);
  }
}
