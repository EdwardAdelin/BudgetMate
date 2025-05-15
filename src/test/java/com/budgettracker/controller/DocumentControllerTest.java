package com.budgettracker.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.budgettracker.repository.UploadedFileRepository;
import com.budgettracker.repository.UserRepository;
import org.springframework.security.core.Authentication;
import static org.mockito.Mockito.when;
import com.budgettracker.model.User;
import java.util.Optional;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(DocumentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class DocumentControllerTest {
    @Autowired
    private MockMvc mockMvc; // Mock web context

    @MockBean
    private UploadedFileRepository uploadedFileRepository;
    @MockBean
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        // Test context loads for DocumentController
    }

    // Smoke test for GET /api/documents/my-files
    @Test
    void getMyFilesReturnsOk() throws Exception {
        // Mock authentication and user
        Authentication auth = org.mockito.Mockito.mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser");
        User user = new User();
        user.setUsername("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        // Perform GET request
        mockMvc.perform(get("/api/documents/my-files").principal(auth))
                .andExpect(status().isOk());
    }
} 