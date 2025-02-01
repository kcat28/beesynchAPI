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

    @PostMapping(value = "/save")
    public String saveUser(@RequestBody User user) {

        userService.saveUser(user);
//            userRepo.save(user);
        return "saved...";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        try {
            System.out.println(userDTO);
            // Step 1: Authenticate the user's credentials
            User user = userRepo.findByUserName(userDTO.getUser_name()); // Find user by username

            // Validate the retrieved user and its password
            if (user == null || !user.getUser_password().equals(userDTO.getUser_password())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Username or Password");
            }

            // Step 2: Generate JWT token for authenticated user
            String token = jwtUtil.generateToken(user.getId()); // Use user ID instead of username

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

    @PostMapping("/verify-recovery-code")
    public ResponseEntity<?> verifyRecoveryCode(@RequestBody UserDTO userDTO) {
        try {
            User user = userRepo.findByUserName(userService.getLoggedInUsername());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User not found.");
            }

            if (!userDTO.getRecovery_code().equals(user.getRecovery_code())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid recovery code.");
            }

            return ResponseEntity.ok("Recovery code verified successfully!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while verifying the recovery code.");
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> resetPassword(@RequestBody UserDTO userDTO) {
        try {
            User user = userRepo.findByUserName(userService.getLoggedInUsername());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User not found.");
            }

            if (!userDTO.getNewPassword().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{8,}$")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Password must:\n• Be at least 8 characters long.\n• Contain at least one uppercase and one lowercase letter.\n• Have at least one numeric digit.");
            }

            user.setUser_password(userDTO.getNewPassword());
            userRepo.save(user);

            return ResponseEntity.ok("Password reset successfully!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while resetting the password.");
        }
    }


}
