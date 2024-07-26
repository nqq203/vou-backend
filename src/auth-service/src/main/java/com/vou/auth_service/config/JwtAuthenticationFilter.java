package com.vou.auth_service.config;

import com.vou.auth_service.model.User;
import com.vou.auth_service.service.AuthenticationService;
import com.vou.auth_service.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    @Autowired
    private AuthenticationService authenticationService;
    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    private static final List<String> AUTH_WHITELIST = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/api/v1/auth/change-password"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            System.out.println("JwtAuthenticationFilter is called");
            String requestPath = request.getRequestURI();
            if (AUTH_WHITELIST.contains(requestPath)) {
                filterChain.doFilter(request, response);
                return; // Return to exit the method
            }

            System.out.println("JwtAuthenticationFilter is called");
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                System.out.println("Token: " + token);
                boolean isValidToken = !authenticationService.isTokenBlackListed(token);
                String username = jwtService.validateTokenAndGetUsername(token);
                System.out.println("In JWT config: " + username + " " + isValidToken);
                if (username != null && isValidToken) {
                    System.out.println("In JWT config: valid token and username");
                    User user = authenticationService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response); // Move this outside the if-block
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("Authentication error", e);
        }
    }
}
