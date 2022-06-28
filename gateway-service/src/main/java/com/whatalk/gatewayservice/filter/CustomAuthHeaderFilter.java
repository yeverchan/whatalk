package com.whatalk.gatewayservice.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
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
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다.");
            }

            String jwtHeader = reqHeaders.get(HttpHeaders.AUTHORIZATION).get(0);

            if (!jwtHeader.startsWith("Bearer ")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 토큰 형식입니다.");
            }

            String jwt = jwtHeader.replace("Bearer ", "");

            try {

                DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(environment.getProperty("jwt.SECRET"))).build().verify(jwt);

                String email = decodedJWT.getSubject();
                String targetEmail = reqHeaders.get("Email").get(0);

                if(email == null || !targetEmail.equals(email)){
                    throw new JWTVerificationException("");
                }

            } catch (JWTVerificationException e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {
    }
}
