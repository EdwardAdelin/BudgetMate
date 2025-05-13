package com.budgettracker.repository;

import com.budgettracker.model.Category;
import com.budgettracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Repository for Category entity
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Additional query methods can be defined here if needed

    // Find all categories for a specific user
    List<Category> findByUser(User user);

    void deleteByUser(User user); // Delete all categories for a user
} 