package com.budgettracker.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService; // Inject service

    @Test
    void contextLoads() {
        // Test context loads for UserService
    }
} 