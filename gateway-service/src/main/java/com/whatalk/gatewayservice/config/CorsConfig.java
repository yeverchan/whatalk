package com.whatalk.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class CorsConfig extends CorsConfiguration {

    private final Environment environment;

    public CorsConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public CorsWebFilter corsFilter() {

        String LOCAL = environment.getProperty("ALLOW.ORIGIN.LOCAL");
        String EC2 = environment.getProperty("ALLOW.ORIGIN.EC2");

        List<String> METHODS = environment.getProperty("ALLOW.METHODS", List.class);
        List<String> ALLOW_HEADERS = environment.getProperty("ALLOW.HEADERS", List.class);
        List<String> EXPOSED_HEADERS = environment.getProperty("EXPOSED.HEADERS", List.class);

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOrigin(LOCAL);
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedMethods(METHODS);
        corsConfiguration.setAllowedHeaders(ALLOW_HEADERS);

        corsConfiguration.setExposedHeaders(EXPOSED_HEADERS);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsWebFilter(source);
    }
}
