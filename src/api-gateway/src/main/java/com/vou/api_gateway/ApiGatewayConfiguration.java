package com.vou.api_gateway;

import org.apache.http.impl.client.AutoRetryHttpClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApiGatewayConfiguration {
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder, WebClient.Builder webClientBuilder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/v1/auth/**")
                        .uri("lb://auth-service")
                )
                .route("user-service", r -> r.path("/api/v1/user/**")
                        .filters(f -> f.filter(new AuthenticationFilter(webClientBuilder)))
                        .uri("lb://user-service")
                )
                .route("notification-service", r -> r.path("/api/v1/notification/**")
                        .filters(f -> f.filter(new AuthenticationFilter(webClientBuilder)))
                        .uri("lb://notification-service")
                )
                .route("event-management-service", r -> r.path("/api/v1/event/**")
                        .filters(f -> f.filter(new AuthenticationFilter(webClientBuilder)))
                        .uri("lb://event-management-service")
                )
                .route("game-streaming-service", r -> r.path("/api/v1/game/**")
                        .filters(f -> f.filter(new AuthenticationFilter(webClientBuilder)))
                        .uri("lb://game-streaming-service")
                )
                .route("inventory-and-rewards-service", r -> r.path("/api/v1/inventory-and-rewards/**")
                        .filters(f -> f.filter(new AuthenticationFilter(webClientBuilder)))
                        .uri("lb://inventory-and-rewards-service")
                )
                .route("statistics-service", r -> r.path("/api/v1/statistic/**")
                        .filters(f -> f.filter(new AuthenticationFilter(webClientBuilder)))
                        .uri("lb://statistics-service")
                )
                .route("turn-service", r -> r.path("/api/v1/turn/**")
                        .filters(f -> f.filter(new AuthenticationFilter(webClientBuilder)))
                        .uri("lb://turn-service")
                )
                .build();
    }

    @Bean
    public AuthenticationFilter customFilter(WebClient.Builder webClientBuilder) {
        return new AuthenticationFilter(webClientBuilder);
    }
}
