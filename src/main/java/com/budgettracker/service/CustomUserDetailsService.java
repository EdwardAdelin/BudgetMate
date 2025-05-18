    package com.budgettracker.service;

    import com.budgettracker.model.User;
    import com.budgettracker.repository.UserRepository;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.stereotype.Service;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

    import java.util.Collections;
    /*
    Implements Spring Security's UserDetailsService
    Loads user details for authentication
    Converts our User entity to Spring Security's UserDetails
    */
    // business logic for user details service
    @Service // Service annotation is used to create a service bean
    public class CustomUserDetailsService implements UserDetailsService {
        private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class); // Logger for logging messages
        private final UserRepository userRepository; // UserRepository is used to find users

        public CustomUserDetailsService(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        @Override // Override annotation is used to override the method
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            logger.debug("Attempting to load user with username: {}", username);
            
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        logger.debug("User not found with username: {}", username);
                        return new UsernameNotFoundException("User not found with username: " + username);
                    });

            logger.debug("User found: {}", user.getUsername()); // Log the user found
            
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(), // Get the username
                    user.getPassword(), // Get the password
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole())) // Get the role
            );
        }
    } 