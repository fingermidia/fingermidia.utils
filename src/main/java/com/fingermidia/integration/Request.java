/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fingermidia.integration;

/**
 *
 * @author dirceubelem
 */
public class Request extends Data {

    private String url;

    public Request() {
        super();
        getHeaders().put("Content-Type", "application/json");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
