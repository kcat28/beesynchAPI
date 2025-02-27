package com.beesynch.app.rest.Controller;

import com.beesynch.app.rest.DTO.UserDTO;
import com.beesynch.app.rest.Models.User;
import com.beesynch.app.rest.Repo.UserRepo;
import com.beesynch.app.rest.Security.JwtUtil;
import com.beesynch.app.rest.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/save")
    public String saveUser(@RequestBody User user) {

        userService.saveUser(user);
        return "saved...";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        try {
            System.out.println(userDTO);
            // Step 1: Authenticate the user's credentials
            User user = userRepo.findByUserName(userDTO.getUser_name()); // Find user by username

            // Validate the retrieved user and its password
            if (user == null || !passwordEncoder.matches(userDTO.getUser_password(), user.getUser_password())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Username or Password");
            }

            // Step 2: Generate JWT token for authenticated user
            String token = jwtUtil.generateToken(user.getId()); // Use user ID instead of username

            // Step 3: Return the token and user information in the response
            return ResponseEntity.ok().body(Map.of(
                    "token", token,
                    "userId", user.getId(),
                    "username", user.getUser_name(),
                    "firstName", user.getFirst_name(),
                    "lastName", user.getLast_name(),
                    "email", user.getUser_email()));

        } catch (Exception e) {
            // Log the exception (use a logger in production)
            e.printStackTrace(); // This will print the error details in your console
            System.out.println("Error during login: " + e.getMessage());

            // Return a meaningful message for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong: " + e.getMessage());
        }
    }

    public record VerifyRecoveryCodeRequestDTO(String userName, String recoveryCode) {
    }

    @PostMapping("/verify-recovery-code")
    public ResponseEntity<?> verifyRecoveryCode(@RequestBody VerifyRecoveryCodeRequestDTO userDTO) {
        try {
            User user = userRepo.findByUserName(userService.getLoggedInUsername());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User not found.");
            }

            if (!userDTO.recoveryCode.equals(user.getRecovery_code())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid recovery code.");
            }

            return ResponseEntity.ok("Recovery code verified successfully!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while verifying the recovery code.");
        }
    }

    public record PasswordChangeRequestDTO(String userName, String oldPassword, String newPassword) {
    }

    // NEW: implemented password encoder
    @PostMapping("/change-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordChangeRequestDTO userDTO) {

        try {
            User user = userRepo.findByUserName(userDTO.userName);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User not found.");
            }

            if (!userDTO.newPassword.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{8,}$")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Password must:\n• Be at least 8 characters long.\n• Contain at least one uppercase and one lowercase letter.\n• Have at least one numeric digit.");
            }

            user.setUser_password(passwordEncoder.encode(userDTO.newPassword)); // Encode the password
            userRepo.save(user);

            return ResponseEntity.ok("Password reset successfully!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while resetting the password.");
        }
    }

    // NEW: for password verifying purposes to see recovery code
    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@RequestBody Map<String, String> request, Principal principal) {
        try {
            String enteredPassword = request.get("password");
            User user = userRepo.findByUserName(principal.getName());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
            }

            if (userService.verifyPassword(enteredPassword, user.getUser_password())) {
                return ResponseEntity.ok("Password verified successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while verifying the password.");
        }
    }

    public record SignupRequest(String user_name, String user_password, String user_email, String first_name,
            String last_name, String recovery_code, boolean is_admin, String security_answers) {
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        try {
            // Convert username to lowercase
            String lowerCaseUsername = signupRequest.user_name().toLowerCase();

            // Log the signup request for debugging
            System.out.println("Sending signup request: " + lowerCaseUsername);

            // Create a new User object
            User user = new User();
            user.setUser_name(lowerCaseUsername);
            user.setUser_password(signupRequest.user_password());
            user.setUser_email(signupRequest.user_email());
            user.setFirst_name(signupRequest.first_name());
            user.setLast_name(signupRequest.last_name());
            user.setRecovery_code(signupRequest.recovery_code());
            user.setIsAdmin(signupRequest.is_admin());
            user.setSecurity_answers(signupRequest.security_answers());

            // Save the user
            userService.saveUser(user);

            return ResponseEntity.ok("User registered successfully!");
        } catch (Exception e) {
            // Log detailed error information
            e.printStackTrace();
            System.out.println("Signup error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong: " + e.getMessage());
        }
    }

    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestParam String username) {
        try {
            User user = userRepo.findByUserName(username);
            if (user == null) {
                return ResponseEntity.ok("Username is available.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already taken.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while checking the username.");
        }
    }

    @GetMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token format");
            }

            String token = authHeader.substring(7); // Remove "Bearer " prefix
            try {
                Long userId = jwtUtil.extractUserId(token);
                User user = userRepo.findById(userId).orElse(null);

                if (user != null) {
                    return ResponseEntity.ok(Map.of(
                            "valid", true,
                            "userId", userId,
                            "username", user.getUser_name()));
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token validation failed");
        }
    }

    // Record for getting security questions by username
    public record GetSecurityQuestionsRequest(String userName) {
    }

    @PostMapping("/get-security-questions")
    public ResponseEntity<?> getSecurityQuestions(@RequestBody GetSecurityQuestionsRequest request) {
        try {
            User user = userRepo.findByUserName(request.userName());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User not found.");
            }

            // Parse the stored security answers JSON
            String securityAnswersJson = user.getSecurity_answers();
            if (securityAnswersJson == null || securityAnswersJson.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No security questions found for this user.");
            }

            // Extract just the questions (not the answers)
            // This assumes the security_answers field contains a JSON with question1,
            // question2, question3
            // We'll use a simple approach to extract just the questions
            Map<String, Object> securityData = new java.util.HashMap<>();
            try {
                // Parse the JSON string into a Map
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                Map<String, Object> fullData = mapper.readValue(securityAnswersJson, Map.class);

                // Extract only the questions
                securityData.put("question1", fullData.get("question1"));
                securityData.put("question2", fullData.get("question2"));
                securityData.put("question3", fullData.get("question3"));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error parsing security questions.");
            }

            return ResponseEntity.ok(securityData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while retrieving security questions.");
        }
    }

    // Record for verifying security answers
    public record VerifySecurityAnswersRequest(String userName, String answer1, String answer2, String answer3) {
    }

    @PostMapping("/verify-security-answers")
    public ResponseEntity<?> verifySecurityAnswers(@RequestBody VerifySecurityAnswersRequest request) {
        try {
            User user = userRepo.findByUserName(request.userName());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User not found.");
            }

            String securityAnswersJson = user.getSecurity_answers();
            if (securityAnswersJson == null || securityAnswersJson.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No security answers found for this user.");
            }

            // Parse the stored security answers
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, String> storedAnswers = mapper.readValue(securityAnswersJson, Map.class);

            // Compare the provided answers with stored answers (case-insensitive)
            boolean isAnswer1Correct = request.answer1().trim().equalsIgnoreCase(storedAnswers.get("answer1").trim());
            boolean isAnswer2Correct = request.answer2().trim().equalsIgnoreCase(storedAnswers.get("answer2").trim());
            boolean isAnswer3Correct = request.answer3().trim().equalsIgnoreCase(storedAnswers.get("answer3").trim());

            if (isAnswer1Correct && isAnswer2Correct && isAnswer3Correct) {
                // Generate a temporary token for password reset
                String resetToken = jwtUtil.generatePasswordResetToken(user.getId());
                return ResponseEntity.ok(Map.of("token", resetToken));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Security answers do not match our records.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while verifying security answers.");
        }
    }

    // Record for resetting password with token
    public record ResetPasswordRequest(String resetToken, String newPassword) {
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPasswordWithToken(@RequestBody ResetPasswordRequest request) {
        try {
            // Validate the reset token
            Long userId = jwtUtil.extractUserIdFromResetToken(request.resetToken());
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid or expired reset token.");
            }

            // Find the user
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Validate the new password
            if (!request.newPassword().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{8,}$")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Password must:\n• Be at least 8 characters long.\n• Contain at least one uppercase and one lowercase letter.\n• Have at least one numeric digit.");
            }

            // Update the password
            user.setUser_password(passwordEncoder.encode(request.newPassword()));
            userRepo.save(user);

            return ResponseEntity.ok("Password has been reset successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while resetting the password.");
        }
    }

}
