package com.budgettracker.controller;

import com.budgettracker.model.Category;
import com.budgettracker.model.User;
import com.budgettracker.repository.CategoryRepository;
import com.budgettracker.repository.UserRepository;
import com.budgettracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;

// REST controller for category operations
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExpenseRepository expenseRepository;

    // Endpoint to save a new category
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category, Authentication authentication) {
        // Get current username
        String username = authentication.getName();
        // Find user by username
        User user = userRepository.findByUsername(username).orElseThrow();
        // Set user to category
        category.setUser(user);
        // Check if category with same name exists for this user
        Category existing = categoryRepository.findByNameAndUser(category.getName(), user);
        if (existing != null) {
            // Category name already exists for this user
            return ResponseEntity.badRequest().body("Category with this name already exists for you.");
        }
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

    // Admin endpoint: get all categories with total spent by all users
    @GetMapping("/admin/overview")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCategoryTotalsForAdmin() {
        // Get category name and total spent for all users
        var results = expenseRepository.findTotalSpentPerCategory();
        // Build response: list of {name, totalSpent}
        var response = results.stream().map(obj -> {
            var map = new java.util.HashMap<String, Object>();
            map.put("name", obj[0]);
            map.put("totalSpent", obj[1]);
            return map;
        }).toList();
        return ResponseEntity.ok(response);
    }
} 