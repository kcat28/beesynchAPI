package com.beesynch.app.rest.controller;
import com.beesynch.app.rest.Controller.AuthController;
import com.beesynch.app.rest.DTO.UserDTO;
import com.beesynch.app.rest.Models.User;
import com.beesynch.app.rest.Repo.UserRepo;
import com.beesynch.app.rest.Security.JwtUtil;
import com.beesynch.app.rest.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testSaveUser() throws Exception {
        User user = new User();
        user.setUser_name("testUser");
        user.setUser_password("password");
        doNothing().when(userService).saveUser(any(User.class));

        mockMvc.perform(post("/auth/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"user_name\":\"testUser\", \"user_password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("saved..."));

        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    public void testLogin_Success() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUser_name("testUser");
        userDTO.setUser_password("password");

        User user = new User();
        user.setId(1L);
        user.setUser_name("testUser");
        user.setUser_password("password");

        when(userRepo.findByUserName("testUser")).thenReturn(user);
        when(jwtUtil.generateToken(1L)).thenReturn("jwt_token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"user_name\":\"testUser\", \"user_password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt_token"));

        verify(userRepo, times(1)).findByUserName("testUser");
        verify(jwtUtil, times(1)).generateToken(1L);
    }

    @Test
    public void testLogin_Failure_InvalidCredentials() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUser_name("testUser");
        userDTO.setUser_password("wrongPassword");

        User user = new User();
        user.setUser_name("testUser");
        user.setUser_password("password");

        when(userRepo.findByUserName("testUser")).thenReturn(user);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"user_name\":\"testUser\", \"user_password\":\"wrongPassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid Username or Password"));

        verify(userRepo, times(1)).findByUserName("testUser");
    }

    @Test
    public void testVerifyRecoveryCode_Success() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setRecovery_code("123456");

        User user = new User();
        user.setUser_name("testUser");
        user.setRecovery_code("123456");

        when(userRepo.findByUserName("testUser")).thenReturn(user);
        when(userService.getLoggedInUsername()).thenReturn("testUser");

        mockMvc.perform(post("/auth/verify-recovery-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"recovery_code\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Recovery code verified successfully!"));

        verify(userRepo, times(1)).findByUserName("testUser");
    }

    @Test
    public void testVerifyRecoveryCode_Failure() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setRecovery_code("wrongCode");

        User user = new User();
        user.setUser_name("testUser");
        user.setRecovery_code("123456");

        when(userRepo.findByUserName("testUser")).thenReturn(user);
        when(userService.getLoggedInUsername()).thenReturn("testUser");

        mockMvc.perform(post("/auth/verify-recovery-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"recovery_code\":\"wrongCode\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid recovery code."));

        verify(userRepo, times(1)).findByUserName("testUser");
    }

    @Test
    public void testResetPassword_Success() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setNewPassword("NewPassword123");

        User user = new User();
        user.setUser_name("testUser");
        user.setUser_password("password");

        when(userRepo.findByUserName("testUser")).thenReturn(user);
        when(userService.getLoggedInUsername()).thenReturn("testUser");

        mockMvc.perform(post("/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"newPassword\":\"NewPassword123\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successfully!"));

        verify(userRepo, times(1)).findByUserName("testUser");
        verify(userRepo, times(1)).save(user);
    }

    @Test
    public void testResetPassword_Failure() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setNewPassword("short");

        User user = new User();
        user.setUser_name("testUser");
        user.setUser_password("password");

        when(userRepo.findByUserName("testUser")).thenReturn(user);
        when(userService.getLoggedInUsername()).thenReturn("testUser");

        mockMvc.perform(post("/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"newPassword\":\"short\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Password must:\n? Be at least 8 characters long.\n? Contain at least one uppercase and one lowercase letter.\n? Have at least one numeric digit."));

        verify(userRepo, times(1)).findByUserName("testUser");
    }
}
