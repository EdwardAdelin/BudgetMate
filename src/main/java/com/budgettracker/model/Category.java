package com.budgettracker.model;

import jakarta.persistence.*;
import lombok.Data;

// Entity for expense categories
@Data
@Entity
@Table(name = "categories")
public class Category {
    // Primary key
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // generates unique id for each category
    private Long id; // id of the category

    // Name of the category (e.g., Transport)
    @Column(nullable = false) // name cannot be null
    private String name; // name of the category

    // Monthly budget for this category
    @Column(nullable = false) // monthly budget cannot be null
    private Double monthlyBudget; // monthly budget of the category

    // The user who owns this category
    @ManyToOne(fetch = FetchType.LAZY) // many categories can belong to one user
    @JoinColumn(name = "user_id", nullable = false) // user_id is the foreign key
    private User user; // user who owns the category    
} 