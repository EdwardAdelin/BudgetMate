package com.budgettracker;

// Import JUnit and Spring Boot test tools
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Annotation to tell Spring Boot to look for the main config
@SpringBootTest
public class BudgetTrackerApplicationTest {
    // Simple test to check if the context loads
    @Test
    void contextLoads() {
        // If the application context fails to load, this test will fail
    }
} 