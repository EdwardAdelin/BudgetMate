package com.budgettracker.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.budgettracker.repository.CategoryRepository;
import com.budgettracker.repository.UserRepository;
import com.budgettracker.repository.ExpenseRepository;
import org.springframework.security.core.Authentication;
import static org.mockito.Mockito.when;
import com.budgettracker.model.User;
import java.util.Optional;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc; // Mock web context

    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ExpenseRepository expenseRepository;

    @Test
    void contextLoads() {
        // Test context loads for CategoryController
    }

    // Smoke test for GET /api/categories
    @Test
    void getCategoriesReturnsOk() throws Exception {
        // Mock authentication and user
        Authentication auth = org.mockito.Mockito.mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser");
        User user = new User();
        user.setUsername("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        // Perform GET request
        mockMvc.perform(get("/api/categories").principal(auth))
                .andExpect(status().isOk());
    }
} 