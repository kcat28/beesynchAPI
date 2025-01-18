package com.beesynch.app.rest.Controller;

import com.beesynch.app.rest.Models.User;
import com.beesynch.app.rest.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
    public class UserController {

        @Autowired
        private UserRepo userRepo;

        @GetMapping(value = "/")
        public String getPage() {
            return "Cheese!";
        }

        @GetMapping(value = "/users")
        public List<User> getUsers() {
            return userRepo.findAll();
        }

        @PostMapping(value = "/save")
        public String saveUser(@RequestBody User user) {
            userRepo.save(user);
            return "saved...";
        }

        @PutMapping(value = "update/{id}")
        public String updateUser(@PathVariable long id, @RequestBody User user) {
            User updatedUser = userRepo.findById(id).get();
            updatedUser.setFirst_name(user.getFirst_name());
            updatedUser.setLast_name(user.getLast_name());
            updatedUser.setUser_name(user.getUser_name());
            updatedUser.setUser_email(user.getUser_email());
            updatedUser.setUser_password(user.getUser_password());
            userRepo.save(updatedUser);
            return "updated...";
        }

        @DeleteMapping(value = "/delete/{id}")
        public String deleteUser(@PathVariable long id) {
            User deleteUser = userRepo.findById(id).get();
            userRepo.delete(deleteUser);
            return "delete user with id: " + id;
        }
    }

