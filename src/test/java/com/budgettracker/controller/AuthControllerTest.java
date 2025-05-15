package com.budgettracker.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.security.core.Authentication;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.budgettracker.service.UserService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc; // Mock web context

    @MockBean
    private UserService userService;

    @Test
    void contextLoads() {
        // Test context loads for AuthController
    }

    // Smoke test for GET /api/auth/me
    @Test
    void getMeReturnsOk() throws Exception {
        // Mock authentication
        Authentication auth = org.mockito.Mockito.mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("testuser");
        mockMvc.perform(get("/api/auth/me").principal(auth))
                .andExpect(status().isOk());
    }
} 