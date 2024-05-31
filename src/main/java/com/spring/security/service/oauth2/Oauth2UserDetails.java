package com.spring.security.service.oauth2;

import java.util.Map;

public  abstract class Oauth2UserDetails {

    protected Map<String ,Object> attributes;

    public abstract String getEmail();
    public Oauth2UserDetails(Map<String,Object> attributes){
        this.attributes = attributes;
    }
public abstract String getName();
}
