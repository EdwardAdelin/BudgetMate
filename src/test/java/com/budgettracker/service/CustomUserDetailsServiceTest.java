package com.budgettracker.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
public class CustomUserDetailsServiceTest {
    @Autowired
    private CustomUserDetailsService customUserDetailsService; // Inject service

    @Test
    void contextLoads() {
        // Test context loads for CustomUserDetailsService
    }
} 