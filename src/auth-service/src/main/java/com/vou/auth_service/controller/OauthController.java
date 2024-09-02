//package com.vou.auth_service.controller;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.oidc.user.OidcUser;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.security.Principal;
//import java.util.Map;
//
//@Controller
//@RequestMapping("/api/v1/oauth")
//@CrossOrigin
//public class OauthController {
//    @GetMapping("/secured")
//    public ResponseEntity<String> securedCall(@AuthenticationPrincipal OidcUser principal) {
//        if (principal == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
//        }
//        String accessToken = principal.getClaims().get("accessToken").toString();
//        return ResponseEntity.ok("Access Token: " + accessToken);
//    }
//
//    @GetMapping("/login")
//    public String redirectToFacebookLoginPage() {
//        return "redirect:/oauth2/authorization/facebook";
//    }
//}
