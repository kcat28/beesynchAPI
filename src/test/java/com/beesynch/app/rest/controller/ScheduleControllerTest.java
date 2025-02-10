//package com.beesynch.app.rest.controller;
//
//import com.beesynch.app.rest.Controller.ScheduleController;
//import com.beesynch.app.rest.Models.Schedule;
//import com.beesynch.app.rest.Models.User;
//import com.beesynch.app.rest.Repo.ScheduleRepo;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Import;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import java.sql.Date;
//import java.sql.Time;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.Optional;
//
//@ExtendWith(MockitoExtension.class)
//public class ScheduleControllerTest {
//
//    @InjectMocks
//    private ScheduleController scheduleController;
//
//    @Mock
//    private ScheduleRepo scheduleRepo;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    public void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(scheduleController).build();
//    }
//
//    @Test
//    public void testFindByTaskId_Success() throws Exception {
//        User user = new User();
//        user.setId(1L);
//
//        Schedule schedule = new Schedule();
//        schedule.setSchedule_id(1L);
//        schedule.setUser_id(user);
//        schedule.setRecurrence("daily");
//        schedule.setDue_time(Time.valueOf(LocalTime.of(10, 0))); // Store as LocalTime
//        schedule.setEnd_date(Date.valueOf(LocalDate.of(2025, 1, 1))); // Store as LocalDate
//        schedule.setStart_date(Date.valueOf(LocalDate.of(2025, 1, 31)));
//
//        when(scheduleRepo.findById(1L)).thenReturn(Optional.of(schedule));
//
//        mockMvc.perform(get("/schedule/task/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.schedule_id").value(1))
//                .andExpect(jsonPath("$.user_id.id").value(1))
//                .andExpect(jsonPath("$.recurrence").value("daily"))
//                .andExpect(jsonPath("$.due_time").value("10:00:00")) // Correct format
//                .andExpect(jsonPath("$.end_date").value("2025-01-01")) // Correct format
//                .andExpect(jsonPath("$.start_date").value("2025-01-31")); // Correct format
//    }
//
//    @Test
//    public void testFindByTaskId_NotFound() throws Exception {
//        when(scheduleRepo.findById(1L)).thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/schedule/task/1"))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    public void testUpdateSchedule_Success() throws Exception {
//        User user = new User();
//        user.setId(1L);
//
//        Schedule existingSchedule = new Schedule();
//        existingSchedule.setSchedule_id(1L);
//        existingSchedule.setUser_id(user);
//        existingSchedule.setRecurrence("weekly");
//        existingSchedule.setDue_time(Time.valueOf(LocalTime.of(12, 0)));
//        existingSchedule.setEnd_date(Date.valueOf(LocalDate.of(2025, 2, 1)));
//        existingSchedule.setStart_date(Date.valueOf(LocalDate.of(2025, 2, 28)));
//
//        when(scheduleRepo.findById(1L)).thenReturn(Optional.of(existingSchedule));
//        when(scheduleRepo.save(any(Schedule.class))).thenReturn(existingSchedule); // Return the same object
//
//        mockMvc.perform(put("/schedule/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\n" +
//                                "\"user_id\": 1,\n" + // User ID in the JSON
//                                "\"recurrence\": \"daily\",\n" +
//                                "\"due_time\": \"10:00:00\",\n" +
//                                "\"start_date\": \"2025-01-31\",\n" +
//                                "\"end_date\": \"2025-01-01\"\n" +
//                                "}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.schedule_id").value(1))
//                .andExpect(jsonPath("$.user_id.id").value(1)) // Check the user ID
//                .andExpect(jsonPath("$.recurrence").value("daily"))
//                .andExpect(jsonPath("$.due_time").value("10:00:00"))
//                .andExpect(jsonPath("$.start_date").value("2025-01-31"))
//                .andExpect(jsonPath("$.end_date").value("2025-01-01"));
//    }
//
//    @Test
//    public void testUpdateSchedule_NotFound() throws Exception {
//        when(scheduleRepo.findById(1L)).thenReturn(Optional.empty());
//
//        mockMvc.perform(put("/schedule/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\n" +
//                                "\"user_id\": 1,\n" +
//                                "\"recurrence\": \"daily\",\n" +
//                                "\"due_time\": \"10:00:00\",\n" +
//                                "\"start_date\": \"2025-01-31\",\n" +
//                                "\"end_date\": \"2025-01-01\"\n" +
//                                "}"))
//                .andExpect(status().isNotFound()); // Expect 404 Not Found
//    }
//}
