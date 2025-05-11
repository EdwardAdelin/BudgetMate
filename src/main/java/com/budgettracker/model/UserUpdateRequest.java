package com.budgettracker.model;

// DTO for updating user info; fields are optional
public class UserUpdateRequest {
    private String username; // Optional new username
    private String email; // Optional new email
    private String password; // Optional new password

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
} 