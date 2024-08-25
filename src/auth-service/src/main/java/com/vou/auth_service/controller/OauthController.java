package com.vou.auth_service.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/oauth")
@CrossOrigin
public class OauthController {
    @GetMapping("/home")
    public String home(@AuthenticationPrincipal OAuth2AuthenticationToken authentication) {
        if (authentication != null) {
            OidcUser user = (OidcUser) authentication.getPrincipal();
            Map<String, Object> attributes = user.getAttributes();

            String username = (String) attributes.get("name");
            String id = (String) attributes.get("id");
            String email = (String) attributes.get("email");

            return "username: " + username + "\nid: " + id + "\nemail: " + email;
        }
        return "nullllllllllllllllllllllllllll";
    }
}
