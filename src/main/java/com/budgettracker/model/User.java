package com.budgettracker.model;

import jakarta.persistence.*;
import lombok.Data;
//Stores user information including username, encrypted password, email, and role
@Data
@Entity
@Table(name = "users")
public class User {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // generates unique id for each user
    private Long id; // id of the user

    @Column(unique = true, nullable = false) // username is unique and cannot be null
    private String username; // username of the user

    @Column(nullable = false) // password cannot be null
    private String password; // password of the user

    @Column(nullable = false) // email cannot be null
    private String email; // email of the user

    @Column(nullable = false) // role cannot be null
    private String role = "USER"; // Default role   
} 