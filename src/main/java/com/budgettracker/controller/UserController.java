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
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UploadedFileRepository uploadedFileRepository;
    // Constructor injection for all repositories
    public UserController(UserRepository userRepository, ExpenseRepository expenseRepository, CategoryRepository categoryRepository, UploadedFileRepository uploadedFileRepository) {
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.uploadedFileRepository = uploadedFileRepository;
    }
    // Returns all usernames as a list
    @GetMapping("/usernames")
    public List<String> getAllUsernames() {
        // Fetch all users and map to usernames
        return userRepository.findAll().stream().map(u -> u.getUsername()).collect(Collectors.toList());
    }
    // Delete user by username
    @DeleteMapping("/{username}")
    @Transactional // Ensure all deletions are in a single transaction
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        var userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            // User not found
            return ResponseEntity.notFound().build();
        }
        var user = userOpt.get();
        // Delete all expenses for user
        expenseRepository.deleteByUser(user);
        // Delete all categories for user
        categoryRepository.deleteByUser(user);
        // Delete all uploaded files for user
        uploadedFileRepository.deleteByUser(user);
        // Delete user
        userRepository.delete(user);
        return ResponseEntity.ok().build(); // Success
    }
} 