package com.vou.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomFilterAccessDenied customAccessDeniedHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CustomFilterAccessDenied customAccessDeniedHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       System.out.println("123");
       http
               .authorizeHttpRequests(authz -> authz
                       .requestMatchers("/api/v1/auth/login",
                               "/api/v1/auth/register",
                               "/api/v1/auth/resend-otp",
                               "/api/v1/auth/validate-token",
                               "/api/v1/auth/verify-otp/**",
                               "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**", "/swagger-resources/**",
                               "/api/v1/oauth").permitAll()
                       .requestMatchers("/api/v1/auth/change-password",
                               "/api/v1/auth/logout").authenticated())
               .csrf(csrf -> csrf.disable())
               .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .oauth2Login(oauth2Login ->
                       oauth2Login
                               .loginPage("/login")
                               .defaultSuccessUrl("/home", true)
                               .userInfoEndpoint(userInfoEndpoint ->
                                       userInfoEndpoint
                                               .oidcUserService(this.oidcUserService())
                               )
               );
       http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
       return http.build();
   }

    private OidcUserService oidcUserService() {
        return new OidcUserService();
    }
}
