package com.fingermidia.integration;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

public class Generic {

    public static Response requestPostSSL(Request req) throws Exception {

        KeyStore keyStore = KeyStore.getInstance("pkcs12");
        InputStream keyStoreInput = new FileInputStream(req.getCertificatePath());
        keyStore.load(keyStoreInput, req.getCertificatePassword().toCharArray());

        SSLContext sslContext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, req.getCertificatePassword().toCharArray())
                .useTLS()
                .build();

        HttpClientBuilder builder = HttpClientBuilder.create();
        SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(
                sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        builder.setSSLSocketFactory(sslConnectionFactory);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslConnectionFactory)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        HttpClientConnectionManager ccm = new BasicHttpClientConnectionManager(registry);
        builder.setConnectionManager(ccm);

        // Perform a sample HTTP request.
        try (CloseableHttpClient httpClient = builder.build()) {

            HttpPost r = new HttpPost(req.getUrl());
            req.getHeaders().entrySet().forEach((entry) -> {
                r.addHeader(entry.getKey(), entry.getValue());
            });
            r.setEntity(new StringEntity(req.getBody()));

            try (CloseableHttpResponse response = httpClient.execute(r)) {
                HttpEntity entity = response.getEntity();
                Response res = getResponse(response);
                EntityUtils.consume(entity);
                return res;
            }

        }
    }

    public static Response requestGetSSL(Request req) throws Exception {

        KeyStore keyStore = KeyStore.getInstance("pkcs12");
        InputStream keyStoreInput = new FileInputStream(req.getCertificatePath());
        keyStore.load(keyStoreInput, req.getCertificatePassword().toCharArray());

        SSLContext sslContext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, req.getCertificatePassword().toCharArray())
                .useTLS()
                .build();

        HttpClientBuilder builder = HttpClientBuilder.create();
        SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(
                sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        builder.setSSLSocketFactory(sslConnectionFactory);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslConnectionFactory)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        HttpClientConnectionManager ccm = new BasicHttpClientConnectionManager(registry);
        builder.setConnectionManager(ccm);

        // Perform a sample HTTP request.
        try (CloseableHttpClient httpClient = builder.build()) {

            HttpGet r = new HttpGet(req.getUrl());
            req.getHeaders().entrySet().forEach((entry) -> {
                r.addHeader(entry.getKey(), entry.getValue());
            });

            try (CloseableHttpResponse response = httpClient.execute(r)) {
                HttpEntity entity = response.getEntity();
                Response res = getResponse(response);
                EntityUtils.consume(entity);
                return res;
            }

        }
    }

    public static Response requestPutSSL(Request req) throws Exception {

        KeyStore keyStore = KeyStore.getInstance("pkcs12");
        InputStream keyStoreInput = new FileInputStream(req.getCertificatePath());
        keyStore.load(keyStoreInput, req.getCertificatePassword().toCharArray());

        SSLContext sslContext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, req.getCertificatePassword().toCharArray())
                .useTLS()
                .build();

        HttpClientBuilder builder = HttpClientBuilder.create();
        SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(
                sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        builder.setSSLSocketFactory(sslConnectionFactory);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslConnectionFactory)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        HttpClientConnectionManager ccm = new BasicHttpClientConnectionManager(registry);
        builder.setConnectionManager(ccm);

        // Perform a sample HTTP request.
        try (CloseableHttpClient httpClient = builder.build()) {

            HttpPut r = new HttpPut(req.getUrl());
            req.getHeaders().entrySet().forEach((entry) -> {
                r.addHeader(entry.getKey(), entry.getValue());
            });
            r.setEntity(new StringEntity(req.getBody()));

            try (CloseableHttpResponse response = httpClient.execute(r)) {
                HttpEntity entity = response.getEntity();
                Response res = getResponse(response);
                EntityUtils.consume(entity);
                return res;
            }

        }
    }

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
