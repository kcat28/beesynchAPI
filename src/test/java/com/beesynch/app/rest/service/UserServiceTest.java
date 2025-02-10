package com.beesynch.app.rest.service;
import com.beesynch.app.rest.Models.User;
import com.beesynch.app.rest.Repo.UserRepo;
import com.beesynch.app.rest.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setFirst_name("John");
        testUser.setLast_name("Doe");
        testUser.setUser_name("johndoe");
        testUser.setUser_email("johndoe@example.com");
        testUser.setUser_password("Password123");
        testUser.setRecovery_code(UUID.randomUUID().toString());
    }

    @Test
    void testGetUserById_UserExists() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(testUser));
        User foundUser = userService.findUserById(1L);
        assertNotNull(foundUser);
        assertEquals("johndoe", foundUser.getUser_name());
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> userService.findUserById(1L));
        assertEquals("User not found with ID: 1", exception.getMessage());
    }

    @Test
    void testSaveUser_Success() {
        when(userRepo.findByUserName(testUser.getUser_name())).thenReturn(null);
        when(userRepo.save(any(User.class))).thenReturn(testUser);

        assertDoesNotThrow(() -> userService.saveUser(testUser));
        verify(userRepo, times(1)).save(testUser);
    }

    @Test
    void testSaveUser_UsernameAlreadyExists() {
        when(userRepo.findByUserName(testUser.getUser_name())).thenReturn(testUser);
        Exception exception = assertThrows(RuntimeException.class, () -> userService.saveUser(testUser));
        assertEquals("Username already exists: johndoe", exception.getMessage());
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(testUser));
        doNothing().when(userRepo).delete(testUser);
        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepo, times(1)).delete(testUser);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
        assertEquals("User not found with ID: 1", exception.getMessage());
    }
}
