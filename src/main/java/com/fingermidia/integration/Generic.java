package com.fingermidia.integration;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Generic {

    public static Response requestPost(Request req) throws Exception {

        HttpPost r = new HttpPost(req.getUrl());

        if (req.getHeaders() != null) {
            req.getHeaders().entrySet().forEach((entry) -> {
                r.addHeader(entry.getKey(), entry.getValue());
            });
        }

        if (req.getBody() != null) {
            HttpEntity entity = new ByteArrayEntity(req.getBody().getBytes("UTF-8"));
            r.setEntity(entity);
        }

        HttpResponse response = HttpClientBuilder.create().build().execute(r);

        Response res = getResponse(response);

        return res;

    }

    public static Response requestGet(Request req) throws Exception {

        HttpGet r = new HttpGet(req.getUrl());

        req.getHeaders().entrySet().forEach((entry) -> {
            r.addHeader(entry.getKey(), entry.getValue());
        });

        HttpResponse response = HttpClientBuilder.create().build().execute(r);

        Response res = getResponse(response);

        return res;

    }

    public static Response getResponse(HttpResponse response) throws IOException {
        int responseCode = response.getStatusLine().getStatusCode();

        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);

        Response r = new Response();
        r.setCode(responseCode);

        r.setBody(body);

        return r;
    }

}
