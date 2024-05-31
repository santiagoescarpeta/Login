package com.spring.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginSuccessController {

    @GetMapping("/loginSuccess")
    public String loginSuccess(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = (OAuth2AuthorizedClient) authentication.getPrincipal();
        OAuth2AccessToken accessToken = client.getAccessToken();
        String tokenValue = accessToken.getTokenValue();

        return "redirect:http://localhost:4200/calendario?token=" + tokenValue;
    }

}