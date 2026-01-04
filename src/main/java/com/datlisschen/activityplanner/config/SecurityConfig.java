package com.datlisschen.activityplanner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // This allows everyone to see every page
                )
                .csrf(csrf -> csrf.disable())    // Disables CSRF for easier development
                .headers(headers -> headers.frameOptions(frame -> frame.disable())); // Allows H2 console to work

        return http.build();
    }
}