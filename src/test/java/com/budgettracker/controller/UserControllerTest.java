package com.budgettracker.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.budgettracker.repository.UserRepository;
import com.budgettracker.repository.ExpenseRepository;
import com.budgettracker.repository.CategoryRepository;
import com.budgettracker.repository.UploadedFileRepository;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc; // Mock web context

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ExpenseRepository expenseRepository;
    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private UploadedFileRepository uploadedFileRepository;

    @Test
    void contextLoads() {
        // Test context loads for UserController
    }

    // Smoke test for GET /api/users/usernames
    @Test
    void getUsernamesReturnsOk() throws Exception {
        mockMvc.perform(get("/api/users/usernames"))
                .andExpect(status().isOk());
    }
} 