package com.spring.security.service.oauth2;

import com.spring.security.entity.Provider;
import com.spring.security.exception.BaseException;

import java.util.Map;

public class Oauth2UserDetailFactory {
    public static Oauth2UserDetails getOauth2UserDetail(String registrationId, Map<String,Object> attributes){
        if (registrationId.equals(Provider.google.name())){
            return new Oauth2GoogleUser(attributes);
        } else if(registrationId.equals(Provider.facebook.name())){
            return new Oauth2FacebookUser(attributes);
        }else {
            throw new BaseException("400", "Perdon! login con"+registrationId+" no esta soportado");
        }
    }
}
