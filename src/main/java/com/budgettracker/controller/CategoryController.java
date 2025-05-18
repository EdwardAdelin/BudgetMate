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
@RestController // RestController annotation is used to create RESTful web services
@RequestMapping("/api/categories") // Maps HTTP requests to handler methods
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository; // CategoryRepository is used to save categories
    @Autowired
    private UserRepository userRepository; // UserRepository is used to find users
    @Autowired
    private ExpenseRepository expenseRepository; // ExpenseRepository is used to find expenses

    // Endpoint to save a new category
    @PostMapping // Maps HTTP POST requests to the createCategory method
    public ResponseEntity<?> createCategory(@RequestBody Category category, Authentication authentication) {
        // Get current username
        String username = authentication.getName(); // Get the username of the current user
        // Find user by username
        User user = userRepository.findByUsername(username).orElseThrow(); // Find the user by username
        // Set user to category
        category.setUser(user); // Set the user to the category
        // Check if category with same name exists for this user
        Category existing = categoryRepository.findByNameAndUser(category.getName(), user); // Check if category with same name exists for this user
        if (existing != null) {
            // Category name already exists for this user
            return ResponseEntity.badRequest().body("Category with this name already exists for you."); // Returns the error message
        }
        // Save category to DB
        Category saved = categoryRepository.save(category); // Save the category to the database
        return ResponseEntity.ok(saved); // Returns the saved category
    }

    // Endpoint to get all categories for the current user
    @GetMapping // Maps HTTP GET requests to the getCategoriesForCurrentUser method
    public ResponseEntity<?> getCategoriesForCurrentUser(Authentication authentication) {
        // Get current username
        String username = authentication.getName();
        // Find user by username
        User user = userRepository.findByUsername(username).orElseThrow(); // Find the user by username
        // Get categories for user
        return ResponseEntity.ok(categoryRepository.findByUser(user)); // Returns the categories for the current user
    }

    // Admin endpoint: get all categories with total spent by all users
    @GetMapping("/admin/overview") // Maps HTTP GET requests to the getCategoryTotalsForAdmin method
    @PreAuthorize("hasRole('ADMIN')") // PreAuthorize annotation is used to restrict access to the method
    public ResponseEntity<?> getCategoryTotalsForAdmin() {
        // Get category name and total spent for all users
        var results = expenseRepository.findTotalSpentPerCategory(); // Get the category name and total spent for all users
        // Build response: list of {name, totalSpent}
        var response = results.stream().map(obj -> { // Build the response
            var map = new java.util.HashMap<String, Object>(); // Create a new map
            map.put("name", obj[0]); // Put the category name in the map
            map.put("totalSpent", obj[1]); // Put the total spent in the map
            return map; // Return the map
        }).toList(); // Convert the map to a list
        return ResponseEntity.ok(response); // Returns the response
    }
} 