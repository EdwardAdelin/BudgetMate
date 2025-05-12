package com.budgettracker.repository;

import com.budgettracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
//Provides database operations for User entity
//Includes methods to find users by username/email and check existence
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
} 