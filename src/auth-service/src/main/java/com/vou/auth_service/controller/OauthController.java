package com.vou.auth_service.controller;

import org.springframework.http.ResponseEntity;
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
    @GetMapping("/secured")
    public ResponseEntity<String> securedCall(@AuthenticationPrincipal OidcUser principal) {
        String accessToken = principal.getClaims().get("accessToken").toString();
        return ResponseEntity.ok("Access Token: " + accessToken);
    }
}
