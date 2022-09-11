package com.dailywords.edgeservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class WebSecurityConfig {
    public static final String EDGE_SERVICE_MANAGER = "EDGE_SERVICE_MANAGER";
    public static final String USER = "EDGE_SERVICE_USER";

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(exchange -> exchange.anyExchange().authenticated())
                .oauth2ResourceServer()
                .jwt();

        http.csrf().and().csrf().disable();
        return http.build();
    }
}