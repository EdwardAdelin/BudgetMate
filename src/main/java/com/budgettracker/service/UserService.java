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
@Service // Service annotation is used to create a service bean
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class); // Logger for logging messages
    private final UserRepository userRepository; // UserRepository is used to find users
    private final PasswordEncoder passwordEncoder; // PasswordEncoder is used to encode passwords

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository; // UserRepository is used to find users
        this.passwordEncoder = passwordEncoder; // PasswordEncoder is used to encode passwords
    }

    @Transactional // Transactional annotation is used to manage transactions
    public User registerUser(User user) {
        logger.debug("Attempting to register new user: {}", user.getUsername()); // Log the attempt to register a new user

        if (userRepository.existsByUsername(user.getUsername())) {
            logger.debug("Registration failed: Username {} already exists", user.getUsername()); // Log the failure to register a new user
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            logger.debug("Registration failed: Email {} already exists", user.getEmail()); // Log the failure to register a new user
            throw new RuntimeException("Email already exists");
        }

        // Encode the password
        String encodedPassword = passwordEncoder.encode(user.getPassword()); // Encode the password
        user.setPassword(encodedPassword); // Set the encoded password
        logger.debug("Password encoded for user: {}", user.getUsername()); // Log the encoded password
        
        // Set default role if not specified
        if (user.getRole() == null) {
            user.setRole("USER"); // Set the default role
            logger.debug("Default role USER assigned to: {}", user.getUsername()); // Log the default role
        }

        User savedUser = userRepository.save(user); // Save the user
        logger.debug("User successfully registered: {}", savedUser.getUsername()); // Log the successful registration
        return savedUser; // Return the saved user
    }

    @Transactional // Transactional annotation is used to manage transactions
    // Updates user fields if present in updateRequest
    public void updateUser(String currentUsername, UserUpdateRequest updateRequest) {
        Optional<User> userOpt = userRepository.findByUsername(currentUsername); // Find the user by username
        if (userOpt.isEmpty()) throw new RuntimeException("User not found"); // Throw an exception if the user is not found
        User user = userOpt.get(); // Get the user
        // Update username if provided
        if (updateRequest.getUsername() != null && !updateRequest.getUsername().isEmpty()) {
            // Check for username uniqueness
            if (userRepository.existsByUsername(updateRequest.getUsername()) && !updateRequest.getUsername().equals(user.getUsername())) {
                throw new RuntimeException("Username already exists"); // Throw an exception if the username already exists
            }
            user.setUsername(updateRequest.getUsername()); // Set the username
        }
        // Update email if provided
        if (updateRequest.getEmail() != null && !updateRequest.getEmail().isEmpty()) {
            // Check for email uniqueness
            if (userRepository.existsByEmail(updateRequest.getEmail()) && !updateRequest.getEmail().equals(user.getEmail())) {
                throw new RuntimeException("Email already exists"); // Throw an exception if the email already exists
            }
            user.setEmail(updateRequest.getEmail()); // Set the email
        }
        // Update password if provided
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateRequest.getPassword())); // Encode the password
        }
        userRepository.save(user); // Save changes
    }
} 