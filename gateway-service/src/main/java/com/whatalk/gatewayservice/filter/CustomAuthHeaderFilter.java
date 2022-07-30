package com.whatalk.gatewayservice.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.whatalk.gatewayservice.exception.ErrorMessage;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@Order(0)
public class CustomAuthHeaderFilter extends AbstractGatewayFilterFactory<CustomAuthHeaderFilter.Config> {

    private final Environment environment;

    public CustomAuthHeaderFilter(Environment environment) {
        super(Config.class);
        this.environment = environment;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            HttpHeaders reqHeaders= request.getHeaders();

            if (!reqHeaders.containsKey(HttpHeaders.AUTHORIZATION) || !reqHeaders.containsKey("Email") ) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.NOT_EXIST_AUTH_HEADER);
            }

            String jwtHeader = reqHeaders.get(HttpHeaders.AUTHORIZATION).get(0);

            if (!jwtHeader.startsWith("Bearer ")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.INVALID_FORMAT_TOKEN);
            }

            String jwt = jwtHeader.replace("Bearer ", "");

            try {

                DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(environment.getProperty("jwt.SECRET"))).build().verify(jwt);

                String email = decodedJWT.getSubject();
                String targetEmail = reqHeaders.get("Email").get(0);

                if(!targetEmail.equals(email)){
                    throw new JWTVerificationException("");
                }

            } catch (JWTVerificationException e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.INVALID_TOKEN);
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {
    }
}
