package com.beesynch.app.rest.controller;
import com.beesynch.app.rest.Controller.UserController;
import com.beesynch.app.rest.Models.User;
import com.beesynch.app.rest.Repo.UserRepo;
import com.beesynch.app.rest.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepo userRepo;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testGetUsers() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setUser_name("user1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUser_name("user2");

        List<User> users = Arrays.asList(user1, user2);
        when(userRepo.findAll()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("[0].user_name").value("user1"))
                .andExpect(jsonPath("[1].user_name").value("user2"));

    }

    @Test
    public void testGetProfile() throws Exception {
        // Mock authentication
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);

        User user = new User();
        user.setUser_name("testuser");
        when(userService.getUserByUsername("testuser")).thenReturn(user);

        mockMvc.perform(get("/users/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_name").value("testuser"));

    }

    @Test
    public void testGetUserById_Success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUser_name("user1");
        when(userService.findUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.user_name").value("user1"));

    }

    @Test
    public void testGetUserById_NotFound() throws Exception {
        when(userService.findUserById(1L)).thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    @Test
    public void testUpdateUser_Success() throws Exception {
        User user = new User();
        user.setUser_name("updatedUser");
        when(userService.updateLoggedInUser(any(User.class))).thenReturn(user);

        mockMvc.perform(put("/users/profile/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"user_name\":\"updatedUser\"}")) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_name").value("updatedUser"));

    }

    @Test
    public void testUpdateUser_NotFound() throws Exception {
        when(userService.updateLoggedInUser(any(User.class))).thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(put("/users/profile/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"updatedUser\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    @Test
    public void testDeleteUser_Success() throws Exception {
        User user = new User();
        user.setId(1L);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(delete("/users/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("delete user with id: 1"));

        verify(userRepo).delete(user);
    }
}
