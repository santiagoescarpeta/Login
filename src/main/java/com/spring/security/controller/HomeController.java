package com.spring.security.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;



@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/index")
    @ResponseBody

    public ResponseEntity<String> hello(Model model, OAuth2AuthenticationToken authentication, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("authentication", auth);
        String tokenValue = getTokenFromHeader(authentication);
        System.out.println(tokenValue);

        setTokenInCookie(response, tokenValue);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:4200/start"))
                .build();
    }

    private String getTokenFromHeader(OAuth2AuthenticationToken authentication) {
        String clientRegistrationId = authentication.getAuthorizedClientRegistrationId();
        String principalName = authentication.getName();
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(clientRegistrationId, principalName);
        OAuth2AccessToken accessToken = client.getAccessToken();
        return accessToken.getTokenValue();
    }

    private void setTokenInCookie(HttpServletResponse response, String tokenValue) {
        Cookie cookie = new Cookie("access_token", tokenValue);
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
    }
}
