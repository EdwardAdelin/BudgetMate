package com.budgettracker.repository;

import com.budgettracker.model.Expense;
import com.budgettracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Repository for Expense entity
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // Find all expenses for a user
    List<Expense> findByUser(User user);
} 