package com.beesynch.app.rest.Service;

import com.beesynch.app.rest.Models.User;
import com.beesynch.app.rest.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Get all users
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public String getLoggedInUsername() {
        // Retrieve the current authenticated user from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // No logged-in user
        }

        return authentication.getName(); // Returns the username (or principal) of the logged-in user
    }

    public Long getLoggedInUserId() {
        // Retrieve the current authenticated user from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // No logged-in user
        }

        // Fetch the user based on the username
        String username = authentication.getName();
        User user = userRepo.findByUserName(username);

        if (user == null) {
            return null; // User not found
        }

        return user.getId(); // Return the user ID
    }

    // generates recovery code for every user signup
    public String generateRecoveryCode() {
        // Generate a unique recovery code using UUID
        return UUID.randomUUID().toString();
    }

    // NEW: for user profile verification of encrypted password entered to see recovery code
    public boolean verifyPassword(String enteredPassword, String storedHashedPassword) {
        return passwordEncoder.matches(enteredPassword, storedHashedPassword);
    }

    // Create a new user
    public void saveUser(User user) {
        System.out.println("Received user details:");
        System.out.println("First Name: " + user.getFirst_name());
        System.out.println("Last Name: " + user.getLast_name());
        System.out.println("Username: " + user.getUser_name());
        System.out.println("Email: " + user.getUser_email());
        System.out.println("Password: " + user.getUser_password()); // Debugging line

        if (userRepo.findByUserName(user.getUser_name()) != null) {
            throw new RuntimeException("Username already exists: " + user.getUser_name());
        }

        user.setUser_password(passwordEncoder.encode(user.getUser_password())); // Hash the password


        user.setRecovery_code(generateRecoveryCode());

        // Save user
        userRepo.save(user);
    }

    // Get a user by ID
    public User findUserById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    // get user profile by username
    public User getUserByUsername(String username) {
        return userRepo.findByUserName(username);
    }

    // Update a user
    public User updateLoggedInUser(User userDetails) {
        // Retrieve logged-in username
        String username = getLoggedInUsername();

        if (username == null) {
            throw new RuntimeException("No user is currently logged in.");
        }

        // Fetch the user based on the username
        User existingUser = userRepo.findByUserName(username);

        if (existingUser == null) {
            throw new RuntimeException("User not found with username: " + username);
        }

        // Update fields (ensure null checks to avoid overwriting with null)
        if (userDetails.getFirst_name() != null) {
            existingUser.setFirst_name(userDetails.getFirst_name());
        }

        if (userDetails.getLast_name() != null) {
            existingUser.setLast_name(userDetails.getLast_name());
        }

        if (userDetails.getUser_email() != null) {
            existingUser.setUser_email(userDetails.getUser_email());
        }

        existingUser.setImg_path(userDetails.getImg_path());


        // Note: Do not allow user to change their username here unless intended
        // Remove this line if username changes are NOT allowed
        if (userDetails.getUser_name() != null) {
            // Check if the new username is already in use by another user
            User existing = userRepo.findByUserName(userDetails.getUser_name());
            if (existing != null && existing.getId() != existingUser.getId()) {
                throw new RuntimeException("Username already exists: " + userDetails.getUser_name());
            }
            existingUser.setUser_name(userDetails.getUser_name());
        }

        // Save the updated user
        return userRepo.save(existingUser);
    }



    // Delete a user
    public void deleteUser(Long id) {
        User user = findUserById(id);
        userRepo.delete(user);
    }

    // Implementing UserDetailsService's method
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Convert your User to UserDetails (used by Spring Security)
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUser_name())
                .password(user.getUser_password()) // Password should be encoded properly
                .roles("USER") // Adjust roles as needed
                .build();
    }

    // Method to load user by ID
    public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
        User user = userRepo.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

        // Convert your User to UserDetails (used by Spring Security)
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUser_name())
                .password(user.getUser_password()) // Password should be encoded properly
                .roles("USER") // Adjust roles as needed
                .build();
    }
}