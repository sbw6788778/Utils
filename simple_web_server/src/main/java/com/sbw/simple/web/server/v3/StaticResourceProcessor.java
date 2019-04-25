package com.sbw.simple.web.server.v3;

import com.sbw.simple.web.server.v3.connector.http.HttpRequest;
import com.sbw.simple.web.server.v3.connector.http.HttpResponse;

import java.io.IOException;

public class StaticResourceProcessor {

    public void process(HttpRequest request, HttpResponse response) {
        try {
            response.sendStaticResource();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
