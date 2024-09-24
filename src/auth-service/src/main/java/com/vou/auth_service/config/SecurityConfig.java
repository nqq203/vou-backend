package com.vou.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
                               "/v3/api-docs/**",
                               "/swagger-ui/**",
                               "/swagger-ui.html").permitAll()
                       .requestMatchers("/api/v1/auth/change-password",
                               "/api/v1/auth/logout").authenticated())
               .csrf(csrf -> csrf.disable())
               .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .exceptionHandling((exception) -> exception.accessDeniedHandler(customAccessDeniedHandler));
//                .oauth2Login(oauth2Login ->
//                oauth2Login
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/home", true)
//                        .userInfoEndpoint(userInfoEndpoint ->
//                                userInfoEndpoint
//                                        .oidcUserService(this.oidcUserService())
//                        )
//        );
       http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
       return http.build();
   }
}
