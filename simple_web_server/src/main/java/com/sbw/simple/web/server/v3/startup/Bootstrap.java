package com.sbw.simple.web.server.v3.startup;


import com.sbw.simple.web.server.v3.connector.http.HttpConnector;

public final class Bootstrap {
  public static void main(String[] args) {
    HttpConnector connector = new HttpConnector();
    connector.start();
  }
}