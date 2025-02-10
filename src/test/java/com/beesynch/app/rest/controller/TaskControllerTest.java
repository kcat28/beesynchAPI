//package com.beesynch.app.rest.controller;
//import com.beesynch.app.rest.Controller.TaskController;
//import com.beesynch.app.rest.DTO.ScheduleDTO;
//import com.beesynch.app.rest.DTO.TaskCreationRequestDTO;
//import com.beesynch.app.rest.Models.Task;
//import com.beesynch.app.rest.Repo.TaskRepo;
//import com.beesynch.app.rest.Service.TaskService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import java.util.Collections;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//
//@ExtendWith(SpringExtension.class)
//public class TaskControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private TaskRepo taskRepo;
//
//    @Mock
//    private TaskService taskService;
//
//    @InjectMocks
//    private TaskController taskController;
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
//    }
//
//    @Test
//    public void testCreateFullTask() throws Exception {
//        TaskCreationRequestDTO request = new TaskCreationRequestDTO();
//        request.setSchedules(Collections.singletonList(new ScheduleDTO())); // Make sure schedules is not empty
//
//        Task mockTask = new Task();
//        when(taskService.createFullTask(any(TaskCreationRequestDTO.class))).thenReturn(mockTask);
//
//        // Perform the POST request
//        mockMvc.perform(post("/tasks/createFullTask")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(request)))
//                .andExpect(status().isOk());
//
//        verify(taskService, times(1)).createFullTask(any(TaskCreationRequestDTO.class));
//    }
//
//
//    @Test
//    public void testCreateFullTaskWithInvalidSchedule() throws Exception {
//        TaskCreationRequestDTO invalidRequest = new TaskCreationRequestDTO();
//        invalidRequest.setSchedules(Collections.emptyList()); // Empty schedules to trigger the validation
//
//        mockMvc.perform(post("/tasks/createFullTask")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(invalidRequest)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Error: schedules must not be empty."));
//    }
//
//
//    @Test
//    public void testGetAllTasks() throws Exception {
//        // Sample data to return
//        when(taskRepo.findAll()).thenReturn(Collections.singletonList(new Task()));
//
//        // Perform the GET request
//        mockMvc.perform(get("/tasks/all-tasks"))
//                .andExpect(status().isOk());
//
//        verify(taskRepo, times(1)).findAll();
//    }
//
//    @Test
//    public void testDeleteTask() throws Exception {
//        long taskId = 1L;
//
//        // Mocking the deletion
//        doNothing().when(taskRepo).deleteById(taskId);
//
//        // Perform the DELETE request
//        mockMvc.perform(delete("/tasks/{taskId}", taskId))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Task deleted with ID: " + taskId));
//
//        verify(taskRepo, times(1)).deleteById(taskId);
//    }
//
//}
