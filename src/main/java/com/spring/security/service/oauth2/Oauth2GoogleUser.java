package com.spring.security.service.oauth2;

import java.util.Map;

public class Oauth2GoogleUser extends Oauth2UserDetails {

    public Oauth2GoogleUser(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName(){
        return (String) attributes.get("name");
    }
}
