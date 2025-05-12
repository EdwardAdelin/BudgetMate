package com.budgettracker.controller;

import com.budgettracker.model.User;
import com.budgettracker.model.UserUpdateRequest;
import com.budgettracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
//Handles registration requests
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/me")
    // Returns the username of the currently authenticated user
    public ResponseEntity<?> getCurrentUser(org.springframework.security.core.Authentication authentication) {
        // If user is authenticated, return their username
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok(authentication.getName());
        } else {
            // If not authenticated, return 401 Unauthorized
            return ResponseEntity.status(401).body("Unauthorized");
        }
    }

    @PutMapping("/update")
    // Updates the authenticated user's info; only non-null fields are updated
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest updateRequest, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        try {
            userService.updateUser(authentication.getName(), updateRequest);
            return ResponseEntity.ok("User updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 