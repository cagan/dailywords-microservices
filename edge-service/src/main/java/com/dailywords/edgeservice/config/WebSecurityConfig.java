package com.dailywords.edgeservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
//@EnableWebSecurity
public class WebSecurityConfig {
    public static final String EDGE_SERVICE_MANAGER = "EDGE_SERVICE_MANAGER";
    public static final String USER = "EDGE_SERVICE_USER";


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
//        http.authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/api/movies", "/api/movies/**", "/actuator/**").permitAll()
//    }
}
