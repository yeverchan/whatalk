package com.whatalk.gatewayservice.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthHeaderFilter extends AbstractGatewayFilterFactory<CustomAuthHeaderFilter.Config> {

    public CustomAuthHeaderFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                // TODO: 2022/06/27 refactor 
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            String token = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

            if (!token.startsWith("Bearer")) {
                // TODO: 2022/06/27 refactor 
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            token = token.replace("Bearer", "");

            // TODO: 2022/06/27 valid 

            return chain.filter(exchange);
        };
    }

    public static class Config {
    }
}
