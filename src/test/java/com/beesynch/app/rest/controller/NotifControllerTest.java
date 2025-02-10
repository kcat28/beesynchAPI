package com.beesynch.app.rest.controller;

import com.beesynch.app.rest.Controller.NotifController;
import com.beesynch.app.rest.Models.Notification;
import com.beesynch.app.rest.Repo.NotificationRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class NotifControllerTest {

    @Mock
    private NotificationRepo notificationRepo;

    @InjectMocks
    private NotifController notifController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(notifController).build();
    }

    @Test
    public void testGetNotificationById_Success() throws Exception {
        Long notifId = 1L;
        Notification notification = new Notification();
        notification.setId(notifId);
        when(notificationRepo.findById(notifId)).thenReturn(Optional.of(notification));

        mockMvc.perform(get("/notifications/getbyId/{notifId}", notifId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(notificationRepo, times(1)).findById(notifId);
    }

    @Test
    public void testGetNotificationById_NotFound() throws Exception {
        Long notifId = 1L;
        when(notificationRepo.findById(notifId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/notifications/getbyId/{notifId}", notifId))
                .andExpect(status().isNotFound());

        verify(notificationRepo, times(1)).findById(notifId);
    }

    @Test
    public void testGetNotificationsByUserId() throws Exception {
        Long userId = 1L;
        List<Notification> notifications = Arrays.asList(new Notification(), new Notification());
        when(notificationRepo.findByUser_id(userId)).thenReturn(notifications);

        mockMvc.perform(get("/notifications/getbyUser/{user_id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(notificationRepo, times(1)).findByUser_id(userId);
    }

    @Test
    public void testGetNotificationsByHiveId() throws Exception {
        Long hiveId = 1L;
        List<Notification> notifications = Arrays.asList(new Notification(), new Notification());
        when(notificationRepo.findbyHive(hiveId)).thenReturn(notifications);

        mockMvc.perform(get("/notifications/getbyHive/{hive_id}", hiveId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(notificationRepo, times(1)).findbyHive(hiveId);
    }
}
