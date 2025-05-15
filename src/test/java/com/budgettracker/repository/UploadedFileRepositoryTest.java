package com.budgettracker.repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UploadedFileRepositoryTest {
    @Autowired
    private UploadedFileRepository uploadedFileRepository; // Inject repository

    @Test
    void contextLoads() {
        // Test context loads for UploadedFileRepository
    }
} 