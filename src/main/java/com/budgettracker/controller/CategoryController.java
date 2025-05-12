package com.budgettracker.controller;

import com.budgettracker.model.Category;
import com.budgettracker.model.User;
import com.budgettracker.repository.CategoryRepository;
import com.budgettracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

// REST controller for category operations
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    // Endpoint to save a new category
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category, Authentication authentication) {
        // Get current username
        String username = authentication.getName();
        // Find user by username
        User user = userRepository.findByUsername(username).orElseThrow();
        // Set user to category
        category.setUser(user);
        // Save category to DB
        Category saved = categoryRepository.save(category);
        return ResponseEntity.ok(saved);
    }

    // Endpoint to get all categories for the current user
    @GetMapping
    public ResponseEntity<?> getCategoriesForCurrentUser(Authentication authentication) {
        // Get current username
        String username = authentication.getName();
        // Find user by username
        User user = userRepository.findByUsername(username).orElseThrow();
        // Get categories for user
        return ResponseEntity.ok(categoryRepository.findByUser(user));
    }
} 