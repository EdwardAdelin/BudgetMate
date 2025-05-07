package com.budgettracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/add-budget.html",
                    "/archive.html",
                    "/dashboard.html",
                    "/dashboard-alt.html",
                    "/admin-users.html",
                    "/add-expenses.html",
                    "/welcome.html",
                    "/profile.html",
                    "/static/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/webjars/**",
                    "/register.html"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin()
            .and()
            .logout();

        return http.build();
    }
} 