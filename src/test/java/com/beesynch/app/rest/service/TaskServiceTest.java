package com.beesynch.app.rest.service;
import com.beesynch.app.rest.DTO.TaskCreationRequestDTO;
import com.beesynch.app.rest.Models.*;
import com.beesynch.app.rest.Repo.*;

import com.beesynch.app.rest.Service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepo taskRepo;

    @Mock
    private ScheduleRepo scheduleRepo;

    @Mock
    private TaskAssignmentRepo taskAssignmentRepo;

    @Mock
    private NotificationRepo notificationRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private TaskService taskService;

    private TaskCreationRequestDTO taskRequest;

    @BeforeEach
    void setUp() {
        taskRequest = new TaskCreationRequestDTO();
        taskRequest.setTitle("Test Task");
        taskRequest.setDescription("Test Description");
        taskRequest.setCategory("Test Category");
        taskRequest.setTask_status("Pending");
        taskRequest.setRewardpts(10);
        taskRequest.setImg_path("test.png");
        taskRequest.setSchedules(Collections.emptyList());
        taskRequest.setAssignments(Collections.emptyList());
    }

    @Test
    void createFullTask_Success() {
        Task task = new Task();
        task.setId(1L);
        when(taskRepo.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.createFullTask(taskRequest);

        assertNotNull(createdTask);
        assertEquals(1L, createdTask.getId());
        verify(taskRepo, times(1)).save(any(Task.class));
    }

    @Test
    void createFullTask_Failure() {
        when(taskRepo.save(any(Task.class))).thenThrow(new RuntimeException("DB Error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskService.createFullTask(taskRequest);
        });

        assertTrue(exception.getMessage().contains("DB Error"));
        verify(taskRepo, times(1)).save(any(Task.class));
    }
}

