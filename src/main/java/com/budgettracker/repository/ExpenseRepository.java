package com.budgettracker.repository;

import com.budgettracker.model.Expense;
import com.budgettracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

// Repository for Expense entity
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // Find all expenses for a user
    List<Expense> findByUser(User user);

    // Find expenses for a user in a specific month and year
    List<Expense> findByUserAndDateBetween(User user, java.time.LocalDate start, java.time.LocalDate end);

    // Sums total spent per category for all users
    @Query("SELECT e.category.name, SUM(e.sum) FROM Expense e GROUP BY e.category.name")
    List<Object[]> findTotalSpentPerCategory();

    void deleteByUser(User user); // Delete all expenses for a user
} 