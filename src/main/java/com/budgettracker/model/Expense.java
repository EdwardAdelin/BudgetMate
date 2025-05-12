package com.budgettracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.math.BigDecimal;
import java.time.LocalDate;

// Expense entity for storing user expenses
@Entity
public class Expense {
    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Expense name
    private String name;

    // Expense sum
    private BigDecimal sum;

    // Expense date
    private LocalDate date;

    // User who owns the expense
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Category of the expense
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getSum() { return sum; }
    public void setSum(BigDecimal sum) { this.sum = sum; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
} 