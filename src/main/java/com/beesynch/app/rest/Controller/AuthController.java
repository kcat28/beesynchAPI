package com.beesynch.app.rest.Controller;

import com.beesynch.app.rest.DTO.UserDTO;
import com.beesynch.app.rest.Models.User;
import com.beesynch.app.rest.Repo.UserRepo;
import com.beesynch.app.rest.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")

public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        try {
            // Delegate login validation to the User
            User user = userRepo.findByUserName(userDTO.getUser_name());

            if (user != null && user.getUser_password().equals(userDTO.getUser_password())) {
                // If validation is successful, return success response
                return ResponseEntity.ok(user);
            } else {
                // If validation fails, return unauthorized response
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Username or Password");
            }
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
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
