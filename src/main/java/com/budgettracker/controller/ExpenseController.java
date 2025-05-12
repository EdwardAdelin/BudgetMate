package com.budgettracker.controller;

import com.budgettracker.model.Expense;
import com.budgettracker.model.User;
import com.budgettracker.model.Category;
import com.budgettracker.repository.ExpenseRepository;
import com.budgettracker.repository.UserRepository;
import com.budgettracker.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

// REST controller for expense operations
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    // Endpoint to save a new expense
    @PostMapping
    public ResponseEntity<Expense> createExpense(@RequestBody Expense expense, Authentication authentication) {
        // Get current username
        String username = authentication.getName();
        // Find user by username
        User user = userRepository.findByUsername(username).orElseThrow();
        // Set user to expense
        expense.setUser(user);
        // Optionally, set category if provided (by id)
        if (expense.getCategory() != null && expense.getCategory().getId() != null) {
            Category category = categoryRepository.findById(expense.getCategory().getId()).orElse(null);
            expense.setCategory(category);
        }
        // Save expense to DB
        Expense saved = expenseRepository.save(expense);
        return ResponseEntity.ok(saved);
    }

    // Endpoint to get all expenses for the current user
    @GetMapping
    public ResponseEntity<?> getExpensesForCurrentUser(Authentication authentication) {
        // Get current username
        String username = authentication.getName();
        // Find user by username
        User user = userRepository.findByUsername(username).orElseThrow();
        // Get expenses for user
        return ResponseEntity.ok(expenseRepository.findByUser(user));
    }
} 