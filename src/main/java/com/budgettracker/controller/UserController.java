package com.budgettracker.controller;

import com.budgettracker.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import com.budgettracker.repository.ExpenseRepository;
import com.budgettracker.repository.CategoryRepository;
import com.budgettracker.repository.UploadedFileRepository;
import jakarta.transaction.Transactional;

// Controller for user-related endpoints
@RestController // RestController annotation is used to create RESTful web services
@RequestMapping("/api/users") // Maps HTTP requests to handler methods
public class UserController {
    private final UserRepository userRepository; // UserRepository is used to find users
    private final ExpenseRepository expenseRepository; // ExpenseRepository is used to find expenses
    private final CategoryRepository categoryRepository; // CategoryRepository is used to find categories
    private final UploadedFileRepository uploadedFileRepository; // UploadedFileRepository is used to find uploaded files
    // Constructor injection for all repositories
    public UserController(UserRepository userRepository, ExpenseRepository expenseRepository, CategoryRepository categoryRepository, UploadedFileRepository uploadedFileRepository) {
        this.userRepository = userRepository; // UserRepository is used to find users
        this.expenseRepository = expenseRepository; // ExpenseRepository is used to find expenses
        this.categoryRepository = categoryRepository; // CategoryRepository is used to find categories
        this.uploadedFileRepository = uploadedFileRepository; // UploadedFileRepository is used to find uploaded files
    }
    // Returns all usernames as a list
    @GetMapping("/usernames") // Maps HTTP GET requests to the getAllUsernames method
    public List<String> getAllUsernames() {
        // Fetch all users and map to usernames
        return userRepository.findAll().stream().map(u -> u.getUsername()).collect(Collectors.toList());
    }
    // Delete user by username
    @DeleteMapping("/{username}") // Maps HTTP DELETE requests to the deleteUser method
    @Transactional // Ensure all deletions are in a single transaction
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        var userOpt = userRepository.findByUsername(username); // Find the user by username
        if (userOpt.isEmpty()) {
            // User not found
            return ResponseEntity.notFound().build(); // Returns a not found response
        }
        var user = userOpt.get(); // Get the user
        // Delete all expenses for user
        expenseRepository.deleteByUser(user); // Delete all expenses for user
        // Delete all categories for user
        categoryRepository.deleteByUser(user); // Delete all categories for user
        // Delete all uploaded files for user
        uploadedFileRepository.deleteByUser(user); // Delete all uploaded files for user
        // Delete user
        userRepository.delete(user);
        return ResponseEntity.ok().build(); // Success
    }
} 