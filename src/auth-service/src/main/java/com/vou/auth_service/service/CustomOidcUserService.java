package com.vou.auth_service.service;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class CustomOidcUserService extends OidcUserService {
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        Set<OidcUserAuthority> authorities = Collections.singleton(new OidcUserAuthority(oidcUser.getIdToken(), oidcUser.getUserInfo()));
        OidcIdToken idToken = userRequest.getIdToken();
        String accessToken = userRequest.getAccessToken().getTokenValue();
        OidcUserInfo userInfo = new OidcUserInfo(Map.of("accessToken", accessToken));
        return new DefaultOidcUser(authorities, idToken, userInfo);
    }
}
