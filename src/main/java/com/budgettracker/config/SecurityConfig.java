package com.budgettracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Keep disabled if handling API calls via fetch/REST without CSRF tokens
                .authorizeHttpRequests(auth -> auth
                        // 1. Allow public static assets
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()

                        // 2. Allow public view routes and auth APIs
                        .requestMatchers("/", "/login", "/register", "/api/auth/**").permitAll()

                        // 3. Restrict Admin-only view routes and APIs
                        .requestMatchers("/dashboard-alt", "/admin-users", "/admin-archive", "/user-list", "/api/admin/**", "/api/users/**").hasRole("ADMIN")

                        // 4. All other routes (e.g., /dashboard, /add-expenses, /add-budget, /archive, /profile) require authentication
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/api/auth/login") // Spune-i lui Spring Security unde trimite formularul cererea POST
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}