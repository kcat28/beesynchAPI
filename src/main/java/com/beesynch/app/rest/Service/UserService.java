package com.beesynch.app.rest.Service;

import com.beesynch.app.rest.Models.User;
import com.beesynch.app.rest.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    // Get all users
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    private String getLoggedInUsername() {
        // Retrieve the current authenticated user from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // No logged-in user
        }

        return authentication.getName(); // Returns the username (or principal) of the logged-in user
    }

    // Create a new user
    public User saveUser(User user) {
        if (userRepo.findByUserName(user.getUser_name()) != null) {
            throw new RuntimeException("Username already exists: " + user.getUser_name());
        }

        // Save user
        return userRepo.save(user);
    }

    // Get a user by ID
    public User findUserById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    // get user profile by suername
    public User getUserByUsername(String username) {
        return userRepo.findByUserName(username);
    }

    // Update a user's information
    public User updateUser(Long id, User userDetails) {
        User existingUser = findUserById(id);

        existingUser.setFirst_name(userDetails.getFirst_name());
        existingUser.setLast_name(userDetails.getLast_name());
        existingUser.setUser_name(userDetails.getUser_name());
        existingUser.setUser_email(userDetails.getUser_email());

        return userRepo.save(existingUser);
    }

    // Change/update password
    public boolean changePassword(String currentPassword, String newPassword) {
        String username = getLoggedInUsername();
        User user = userRepo.findByUserName(username);

        if (user == null || !currentPassword.equals(user.getUser_password())) {
            return false; // Either user not found or current password doesn't match
        }

        user.setUser_password(newPassword);
        userRepo.save(user);
        return true;
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
}