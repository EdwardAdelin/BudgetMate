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
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // generates unique id for each expense
    private Long id; // id of the expense

    // Expense name
    private String name; // name of the expense

    // Expense sum
    private BigDecimal sum; // sum of the expense

    // Expense date
    private LocalDate date; // date of the expense

    // User who owns the expense
    @ManyToOne // many expenses can belong to one user
    @JoinColumn(name = "user_id") // user_id is the foreign key
    private User user; // user who owns the expense

    // Category of the expense
    @ManyToOne // many expenses can belong to one category
    @JoinColumn(name = "category_id") // category_id is the foreign key
    private Category category; // category of the expense

    // Getters and setters
    public Long getId() { return id; } // get the id of the expense
    public void setId(Long id) { this.id = id; } // set the id of the expense

    public String getName() { return name; } // get the name of the expense
    public void setName(String name) { this.name = name; } // set the name of the expense

    public BigDecimal getSum() { return sum; } // get the sum of the expense
    public void setSum(BigDecimal sum) { this.sum = sum; } // set the sum of the expense

    public LocalDate getDate() { return date; } // get the date of the expense
    public void setDate(LocalDate date) { this.date = date; } // set the date of the expense

    public User getUser() { return user; } // get the user of the expense
    public void setUser(User user) { this.user = user; } // set the user of the expense

    public Category getCategory() { return category; } // get the category of the expense
    public void setCategory(Category category) { this.category = category; } // set the category of the expense
} 