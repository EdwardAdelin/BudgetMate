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
    private ExpenseRepository expenseRepository; // ExpenseRepository is used to save expenses
    @Autowired
    private UserRepository userRepository; // UserRepository is used to find users
    @Autowired
    private CategoryRepository categoryRepository; // CategoryRepository is used to find categories

    // Endpoint to save a new expense
    @PostMapping // Maps HTTP POST requests to the createExpense method
    public ResponseEntity<Expense> createExpense(@RequestBody Expense expense, Authentication authentication) {
        // Get current username
        String username = authentication.getName(); // Get the username of the current user
        // Find user by username
        User user = userRepository.findByUsername(username).orElseThrow(); // Find the user by username
        // Set user to expense
        expense.setUser(user); // Set the user to the expense
        // Optionally, set category if provided (by id)
        if (expense.getCategory() != null && expense.getCategory().getId() != null) {
            Category category = categoryRepository.findById(expense.getCategory().getId()).orElse(null);
            expense.setCategory(category); // Set the category to the expense
        }
        // Save expense to DB
        Expense saved = expenseRepository.save(expense); // Save the expense to the database
        return ResponseEntity.ok(saved); // Returns the saved expense
    }

    // Endpoint to get all expenses for the current user
    @GetMapping // Maps HTTP GET requests to the getExpensesForCurrentUser method
    public ResponseEntity<?> getExpensesForCurrentUser(Authentication authentication) {
        // Get current username
        String username = authentication.getName(); // Get the username of the current user
        // Find user by username
        User user = userRepository.findByUsername(username).orElseThrow(); // Find the user by username
        // Get expenses for user
        return ResponseEntity.ok(expenseRepository.findByUser(user)); // Returns the expenses for the current user
    }

    // Endpoint to get expenses for a specific month and year for the current user
    @GetMapping("/by-month") // Maps HTTP GET requests to the getExpensesByMonth method
    public ResponseEntity<?> getExpensesByMonth(
            @RequestParam int year, // Get the year
            @RequestParam int month, // Get the month
            Authentication authentication) {
        String username = authentication.getName(); // Get the username of the current user
        User user = userRepository.findByUsername(username).orElseThrow(); // Find the user by username
        java.time.LocalDate start = java.time.LocalDate.of(year, month, 1); // Create a new local date for the start of the month
        java.time.LocalDate end = start.withDayOfMonth(start.lengthOfMonth()); // Create a new local date for the end of the month
        return ResponseEntity.ok(expenseRepository.findByUserAndDateBetween(user, start, end)); // Returns the expenses for the current user
    }
} 