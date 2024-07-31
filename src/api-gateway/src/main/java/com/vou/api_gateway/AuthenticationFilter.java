package com.vou.api_gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class AuthenticationFilter implements GatewayFilter, Ordered {
    private final WebClient.Builder webClientBuilder;

    public AuthenticationFilter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Check if the route requires authentication
        if (requiresAuthentication(request)) {
            String token = request.getHeaders().getFirst("Authorization");

            return webClientBuilder.build()
                    .post()
                    .uri("http://26.95.177.142:8081/api/v1/auth/validate-token")
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .flatMap(isValid -> {
                        System.out.println(isValid);
                        if (Boolean.TRUE.equals(isValid)) {
                            return chain.filter(exchange);
                        } else {
                            ServerHttpResponse response = exchange.getResponse();
                            response.setStatusCode(HttpStatus.UNAUTHORIZED);
                            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

                            DataBufferFactory bufferFactory = response.bufferFactory();
                            DataBuffer dataBuffer = bufferFactory.wrap(createErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Invalid token or session expired"));

                            return response.writeWith(Mono.just(dataBuffer));
                        }
                    });
        }

        return chain.filter(exchange);
    }

    private byte[] createErrorResponse(int code, String message) {
        String json = String.format("{\"code\": %d, \"message\": \"%s\"}", code, message);
        return json.getBytes(StandardCharsets.UTF_8);
    }

    private boolean requiresAuthentication(ServerHttpRequest request) {
        // Define logic to determine if the route requires authentication
        String path = request.getURI().getPath();
        return path.startsWith("/api/v1/");
    }

    @Override
    public int getOrder() {
        return -1; // High precedence
    }
}
