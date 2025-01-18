package com.beesynch.app.rest.Service;

import com.beesynch.app.rest.Models.User;
import com.beesynch.app.rest.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepo userRepo;

//    @Autowired
//    private PasswordEncoder passwordEncoder;


//    @Autowired
//    private PasswordEncoder passwordEncoder;

    // Get all users
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    private String getLoggedInUsername() {
        // Use Spring Security's Authentication context to get the logged-in user (replace with this as needed)
        // Example: return SecurityContextHolder.getContext().getAuthentication().getName();
        return "dangsyana";
    }

    // Create a new user
    public User saveUser(User user) {
        // Check for duplicates (e.g., same email or username)
        if (userRepo.findByUserName(user.getUser_name()) != null) {
            throw new RuntimeException("Username already exists: " + user.getUser_name());
        }
//        if (userRepo.findByUserEmail(user.getUser_email()) != null) {
//            throw new RuntimeException("Email already exists: " + user.getUser_email());
//        }

        // Encrypt the password
//        user.setUser_password(passwordEncoder.encode(user.getUser_password()));

        // Save user
        return userRepo.save(user);
    }

    // Get a user by ID
    public User findUserById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    // Update a user's information
    public User updateUser(Long id, User userDetails) {

//        if (userRepo.findByUserName(user.getUser_name()) != null) {
//            throw new RuntimeException("Username already exists: " + user.getUser_name());
//        }

        User existingUser = findUserById(id);

        existingUser.setFirst_name(userDetails.getFirst_name());
        existingUser.setLast_name(userDetails.getLast_name());
        existingUser.setUser_name(userDetails.getUser_name());
        existingUser.setUser_email(userDetails.getUser_email());

        return userRepo.save(existingUser);
    }

    // change/update password
    public boolean changePassword(String currentPassword, String newPassword) {
        // Get the currently logged-in user (this could be fetched from security context)
        String username = getLoggedInUsername(); // Replace this with your logic to fetch authenticated user
        User user = userRepo.findByUserName(username);

        // Validate current password
        if (user == null || !currentPassword.equals(user.getUser_password())) {
            return false; // Either user not found or current password doesn't match
        }

        // Validate the new password (optional: ensure it meets requirements, etc.)
        if (newPassword.length() < 8) { // Example password policy: at least 6 characters
            throw new IllegalArgumentException("New password must be at least 6 characters.");
        }
        boolean hasUppercase = newPassword.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = newPassword.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = newPassword.chars().anyMatch(Character::isDigit);
        boolean hasSpecialChar = newPassword.chars().anyMatch(ch -> "!@#$%^&*()".indexOf(ch) >= 0);

        if (hasUppercase && hasLowercase && hasDigit && hasSpecialChar) {
            user.setUser_password(newPassword);
            userRepo.save(user);
            return true;
        }

        return false;
    }

    // Delete a user
    public void deleteUser(Long id) {
        User user = findUserById(id);
        userRepo.delete(user);
    }


}