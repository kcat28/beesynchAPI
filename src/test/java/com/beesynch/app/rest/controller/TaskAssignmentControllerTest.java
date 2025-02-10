//package com.beesynch.app.rest.controller;
//
//import com.beesynch.app.rest.Controller.TaskAssignmentController;
//import com.beesynch.app.rest.Models.TaskAssignment;
//import com.beesynch.app.rest.Models.User;
//import com.beesynch.app.rest.Repo.TaskAssignmentRepo;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.mockito.Mockito.verify;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import java.sql.Date;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.Arrays;
//import java.util.List;
//
//@ExtendWith(MockitoExtension.class)
//public class TaskAssignmentControllerTest {
//
//    @InjectMocks
//    private TaskAssignmentController taskAssignmentController;
//
//    @Mock
//    private TaskAssignmentRepo taskAssignmentRepo;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    public void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(taskAssignmentController).build();
//    }
//
//    // Test for GET /tasks/taskAssignment/user/{userId}
//    @Test
//    public void testGetTasksForUser_Success() throws Exception {
//        // Setup user and task assignments
//        User user = new User();
//        user.setId(1L);
//
//        // Explicitly using LocalDate and converting to java.sql.Date
//        TaskAssignment taskAssignment1 = new TaskAssignment();
//        taskAssignment1.setId(1L);
//        taskAssignment1.setUser(user);
//        taskAssignment1.setAssignedDate(Date.valueOf(LocalDate.of(2023, 12, 1)));
//        System.out.println("Task 1 assignedDate: " + taskAssignment1.getAssignedDate());
//
//        TaskAssignment taskAssignment2 = new TaskAssignment();
//        taskAssignment2.setId(2L);
//        taskAssignment2.setUser(user);
//        taskAssignment2.setAssignedDate(Date.valueOf(LocalDate.of(2023, 12, 2)));  // Explicitly use LocalDate
//        System.out.println("Task 2 assignedDate: " + taskAssignment2.getAssignedDate());
//
//
//
//
//        List<TaskAssignment> taskAssignments = Arrays.asList(taskAssignment1, taskAssignment2);
//
//        // Mock repository call
//        when(taskAssignmentRepo.findByUserId(1L)).thenReturn(taskAssignments);
//
//        // Perform the GET request
//        mockMvc.perform(get("/tasks/taskAssignment/user/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2))  // Check number of items
//                .andExpect(jsonPath("[0].user.id").value(1))  // Check for user.id in the response
//                .andExpect(jsonPath("[0].assignedDate").value("2023-12-01"))  // Ensure the date is in the correct format
//                .andExpect(jsonPath("[1].user.id").value(1))  // Check for user.id in the response
//                .andExpect(jsonPath("[1].assignedDate").value("2023-12-02"));
//    }
//
//
//
//    // Test for DELETE /tasks/taskAssignment/{assignmentId}
//    @Test
//    public void testUnassignTask_Success() throws Exception {
//        Long assignmentId = 1L;
//
//        // Simulate the existence of the assignment
//        when(taskAssignmentRepo.existsById(assignmentId)).thenReturn(true);
//
//        mockMvc.perform(delete("/tasks/taskAssignment/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Task unassigned with assignment ID: 1"));
//
//        verify(taskAssignmentRepo).deleteById(assignmentId);
//    }
//
//    // Test for DELETE /tasks/taskAssignment/{assignmentId} - Not Found
//    @Test
//    public void testUnassignTask_NotFound() throws Exception {
//        Long assignmentId = 999L;
//
//        when(taskAssignmentRepo.existsById(assignmentId)).thenReturn(false);  // Simulate that the assignment does not exist
//
//        mockMvc.perform(delete("/tasks/taskAssignment/999"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Assignment ID not found"));
//    }
//}
