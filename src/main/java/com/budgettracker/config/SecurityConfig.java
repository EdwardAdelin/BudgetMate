package com.budgettracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import java.io.IOException;

@Configuration
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                // Public pages: login, register, static
                .requestMatchers(
                    "/login.html",
                    "/register.html",
                    "/api/auth/register",
                    "/api/auth/login",
                    "/static/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/webjars/**"
                ).permitAll()
                // Admin-only pages
                .requestMatchers("/admin-users.html", "/dashboard-alt.html", "/admin-archive.html", "/user-list.html").hasRole("ADMIN")
                // All other pages: only for authenticated non-admins
                .requestMatchers(
                    "/dashboard.html",
                    "/archive.html",
                    "/profile.html",
                    "/add-expenses.html",
                    "/add-budget.html"
                ).hasAnyRole("USER", "PRO_USER")
                // Any other request must be authenticated
                .anyRequest().authenticated()
        );
        
        http.formLogin(form -> form
                .loginPage("/login.html")
                .loginProcessingUrl("/api/auth/login")
                .successHandler(customSuccessHandler())
                .permitAll()
        );
        
        http.logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .logoutSuccessUrl("/login.html")
                .permitAll()
        );
        
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                if (isAdmin) {
                    response.sendRedirect("/admin-users.html");
                } else {
                    response.sendRedirect("/profile.html");
                }
            }
        };
    }
} 