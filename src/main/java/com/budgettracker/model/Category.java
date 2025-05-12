package com.budgettracker.model;

import jakarta.persistence.*;
import lombok.Data;

// Entity for expense categories
@Data
@Entity
@Table(name = "categories")
public class Category {
    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Name of the category (e.g., Transport)
    @Column(nullable = false, unique = true)
    private String name;

    // Monthly budget for this category
    @Column(nullable = false)
    private Double monthlyBudget;

    // The user who owns this category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
} 