package com.vou.api_gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.pattern.PathPatternParser;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class ApiGatewayConfiguration {
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder, WebClient.Builder webClientBuilder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/v1/auth/**")
                        .uri("lb://auth-service")
                )
                .route("user-service", r -> r.path("/api/v1/users/**", "/api/v1/brands/**", "/api/v1/players/**", "/api/v1/admins/**", "/api/v1/sessions/**")
                        .filters(f -> f.filter(new AuthenticationFilter(webClientBuilder)))
                        .uri("lb://user-service")
                )
                .route("event-service", r -> r.path("/api/v1/events/**", "/api/v1/brands-cooperation/**")
                        .filters(f -> f.filter(new AuthenticationFilter(webClientBuilder)))
                        .uri("lb://event-service")
                )
                .route("streaming-service", r -> r.path("/api/v1/game/**")
                        .filters(f -> f.filter(new AuthenticationFilter(webClientBuilder)))
                        .uri("lb://streaming-service")
                )
                .route("inventory-and-reward-service", r -> r.path("/api/v1/vouchers/**", "/api/v1/items/**, /api/v1/item-repos/**")
                        .filters(f -> f.filter(new AuthenticationFilter(webClientBuilder)))
                        .uri("lb://inventory-and-reward-service")
                )
                .route("statistics-service", r -> r.path("/api/v1/statistic/**")
                        .filters(f -> f.filter(new AuthenticationFilter(webClientBuilder)))
                        .uri("lb://statistics-service")
                )
//                .route("turn-service", r -> r.path("/api/v1/turn/**")
//                        .filters(f -> f.filter(new AuthenticationFilter(webClientBuilder)))
//                        .uri("lb://turn-service")
//                )
                .build();
    }

    @Bean
    public AuthenticationFilter customFilter(WebClient.Builder webClientBuilder) {
        return new AuthenticationFilter(webClientBuilder);
    }


    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:8000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
