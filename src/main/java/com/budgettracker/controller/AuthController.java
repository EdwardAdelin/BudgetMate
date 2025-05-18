package com.budgettracker.controller;

import com.budgettracker.model.User;
import com.budgettracker.model.UserUpdateRequest;
import com.budgettracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
//Handles registration requests
@RestController // RestController annotation is used to create RESTful web services
@RequestMapping("/api/auth") // Maps HTTP requests to handler methods
public class AuthController {

    private final UserService userService; // UserService is used to register users

    public AuthController(UserService userService) {  // Constructor for AuthController
        this.userService = userService; // UserService is used to register users
    }

    @PostMapping("/register") // Maps HTTP POST requests to the registerUser method
    public ResponseEntity<?> registerUser(@RequestBody User user) { 
        try {
            User registeredUser = userService.registerUser(user); // UserService is used to register users
            return ResponseEntity.ok(registeredUser); // Returns the registered user
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Returns the error message
        }
    }

    @GetMapping("/me") // Maps HTTP GET requests to the getCurrentUser method
    // Returns the username of the currently authenticated user
    public ResponseEntity<?> getCurrentUser(org.springframework.security.core.Authentication authentication) {
        // If user is authenticated, return their username
        if (authentication != null && authentication.isAuthenticated()) { // If the user is authenticated
            return ResponseEntity.ok(authentication.getName()); // Returns the username of the currently authenticated user
        } else {
            // If not authenticated, return 401 Unauthorized
            return ResponseEntity.status(401).body("Unauthorized"); // Returns the error message
        }
    }

    @PutMapping("/update") // Maps HTTP PUT requests to the updateUser method
    // Updates the authenticated user's info; only non-null fields are updated
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest updateRequest, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) { // If the user is not authenticated
            return ResponseEntity.status(401).body("Unauthorized"); // Returns the error message
        }
        try {
            userService.updateUser(authentication.getName(), updateRequest); // Updates the authenticated user's info; only non-null fields are updated
            return ResponseEntity.ok("User updated successfully"); // Returns the success message
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Returns the error message
        }
    }
} 