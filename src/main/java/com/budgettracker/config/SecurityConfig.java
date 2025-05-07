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
                    "/login.html",
                    "/register.html",
                    "/static/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/webjars/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin()
                .loginPage("/login.html")
                .defaultSuccessUrl("/dashboard.html")
                .permitAll()
            .and()
            .logout()
                .logoutSuccessUrl("/login.html")
                .permitAll();

        return http.build();
    }
} 