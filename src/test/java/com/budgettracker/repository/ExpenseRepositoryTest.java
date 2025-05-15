package com.budgettracker.repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;

@DataJpaTest
public class ExpenseRepositoryTest {
    @Autowired
    private ExpenseRepository expenseRepository; // Inject repository

    @Test
    void contextLoads() {
        // Test context loads for ExpenseRepository
    }
} 