package com.beesynch.app.rest.Controller;

import com.beesynch.app.rest.DTO.UserDTO;
import com.beesynch.app.rest.Models.User;
import com.beesynch.app.rest.Repo.UserRepo;
import com.beesynch.app.rest.Security.JwtUtil;
import com.beesynch.app.rest.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")

public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        try {
            // Step 1: Authenticate the user's credentials
            User user = userRepo.findByUserName(userDTO.getUser_name()); // Find user by username

            // Validate the retrieved user and its password
            if (user == null || !user.getUser_password().equals(userDTO.getUser_password())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Username or Password");
            }

            // Step 2: Generate JWT token for authenticated user
            String token = jwtUtil.generateToken(user.getUser_name());

            // Step 3: Return the token in the response
            return ResponseEntity.ok().body(
                    Map.of("token", token) // Returns the token and the user info
            );

        } catch (Exception e) {
            // Log the exception (use a logger in production)
            e.printStackTrace(); // This will print the error details in your console
            System.out.println("Error during login: " + e.getMessage());

            // Return a meaningful message for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong: " + e.getMessage());
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody UserDTO userDTO) {
        try {
            // Delegate password changing to the UserService
            boolean isChanged = userService.changePassword(
                    userDTO.getCurrentPassword(),
                    userDTO.getNewPassword()
            );

            if (isChanged) {
                return ResponseEntity.ok("Password changed successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Current password is incorrect or new password is invalid.");
            }

        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while changing the password.");
        }
    }
}
