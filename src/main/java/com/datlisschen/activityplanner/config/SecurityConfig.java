package com.datlisschen.activityplanner.config;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.time.LocalDate;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for local development and H2 Console
                .csrf(csrf -> csrf.disable())

                // Configure Authorization
                .authorizeHttpRequests(auth -> auth
                        // Specific static and upload paths
                        .requestMatchers("/css/**", "/js/**", "/uploads/**").permitAll()
                        // Specific application paths
                        .requestMatchers("/", "/idea/**", "/expedition/**").permitAll()
                        // Allow anything else (like H2 console or remaining routes)
                        .anyRequest().permitAll()
                )

                // Disable Login UI and Basic Auth for a "Kiosk" style app
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // Allow H2 Console to display in frames (if you use it)
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }
}