package com.github.mkapiczy.oauth_client;

public class ResourceResponse {
    public String appOwner;


    public ResourceResponse(String appOwner, String refreshToken) {
        this.appOwner = appOwner;
    }
}
