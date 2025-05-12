package com.budgettracker.service;

import com.budgettracker.model.User;
import com.budgettracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.budgettracker.model.UserUpdateRequest;
import java.util.Optional;
/*
Handles user registration
Encrypts passwords using BCrypt
Validates username and email uniqueness
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(User user) {
        logger.debug("Attempting to register new user: {}", user.getUsername());

        if (userRepository.existsByUsername(user.getUsername())) {
            logger.debug("Registration failed: Username {} already exists", user.getUsername());
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            logger.debug("Registration failed: Email {} already exists", user.getEmail());
            throw new RuntimeException("Email already exists");
        }

        // Encode the password
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        logger.debug("Password encoded for user: {}", user.getUsername());
        
        // Set default role if not specified
        if (user.getRole() == null) {
            user.setRole("USER");
            logger.debug("Default role USER assigned to: {}", user.getUsername());
        }

        User savedUser = userRepository.save(user);
        logger.debug("User successfully registered: {}", savedUser.getUsername());
        return savedUser;
    }

    @Transactional
    // Updates user fields if present in updateRequest
    public void updateUser(String currentUsername, UserUpdateRequest updateRequest) {
        Optional<User> userOpt = userRepository.findByUsername(currentUsername);
        if (userOpt.isEmpty()) throw new RuntimeException("User not found");
        User user = userOpt.get();
        // Update username if provided
        if (updateRequest.getUsername() != null && !updateRequest.getUsername().isEmpty()) {
            // Check for username uniqueness
            if (userRepository.existsByUsername(updateRequest.getUsername()) && !updateRequest.getUsername().equals(user.getUsername())) {
                throw new RuntimeException("Username already exists");
            }
            user.setUsername(updateRequest.getUsername());
        }
        // Update email if provided
        if (updateRequest.getEmail() != null && !updateRequest.getEmail().isEmpty()) {
            // Check for email uniqueness
            if (userRepository.existsByEmail(updateRequest.getEmail()) && !updateRequest.getEmail().equals(user.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            user.setEmail(updateRequest.getEmail());
        }
        // Update password if provided
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }
        userRepository.save(user); // Save changes
    }
} 