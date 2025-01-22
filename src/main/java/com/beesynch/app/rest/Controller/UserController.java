package com.beesynch.app.rest.Controller;

import com.beesynch.app.rest.Models.User;
import com.beesynch.app.rest.Repo.UserRepo;
import com.beesynch.app.rest.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController()
@RequestMapping("/users")
    public class UserController {

        @Autowired
        private UserRepo userRepo;

        @Autowired
        private UserService userService;

        @GetMapping(value = "/")
        public String getPage() {
            return "Cheese!";
        }

        @GetMapping()
        public List<User> getUsers() {
            return userRepo.findAll();
        }

        @GetMapping("/profile")
        public User getProfile() {
            // Extract username from SecurityContext
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println("Logged-in username: " + username);
            // Fetch user details using the username
            return userService.getUserByUsername(username);
        }

        // Get a user by ID
        @GetMapping("/{id}")
        public ResponseEntity<Object> getUserById(@PathVariable long id) {
            try {
                User user = userService.findUserById(id);
                return ResponseEntity.ok(user);
            } catch (RuntimeException e) {
                return ResponseEntity.status(404).body(e.getMessage()); // User not found
            }
        }

        @PostMapping(value = "/save")
        public String saveUser(@RequestBody User user) {
            userRepo.save(user);
            return "saved...";
        }

        // Update a user
        @PutMapping("/profile/edit")
        public ResponseEntity<Object> updateUser(@RequestBody User userDetails) {
            try {
                // Update user info based on the logged-in user's data
                User updatedUser = userService.updateLoggedInUser(userDetails);
                return ResponseEntity.ok(updatedUser);
            } catch (RuntimeException e) {
                // Handle errors such as "User not found", validation errors, etc.
                return ResponseEntity.status(404).body(e.getMessage());
            }
        }

        @DeleteMapping(value = "/delete/{id}")
        public String deleteUser(@PathVariable long id) {
            User deleteUser = userRepo.findById(id).get();
            userRepo.delete(deleteUser);
            return "delete user with id: " + id;
        }
    }

