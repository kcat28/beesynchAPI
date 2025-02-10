package com.beesynch.app.rest.controller;

import com.beesynch.app.rest.Controller.BillController;
import com.beesynch.app.rest.DTO.BillDTO;
import com.beesynch.app.rest.DTO.ScheduleDTO;
import com.beesynch.app.rest.Models.Bill;
import com.beesynch.app.rest.Models.Schedule;
import com.beesynch.app.rest.Repo.BillRepo;
import com.beesynch.app.rest.Service.BillService;
import com.fasterxml.jackson.databind.ObjectMapper; // Import ObjectMapper
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BillControllerTest {

    @Mock
    private BillService billService;

    @Mock
    private BillRepo billRepo;

    @InjectMocks
    private BillController billController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper; // Add ObjectMapper

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(billController).build();
        objectMapper = new ObjectMapper(); // Initialize ObjectMapper
    }

    @Test
    public void testCreateBill_Success() throws Exception {
        BillDTO billDTO = new BillDTO();
        billDTO.setBill_name("Electricity Bill");
        billDTO.setAmount(100.0);
        billDTO.setBill_status("Paid");

        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setDueTime(Time.valueOf("14:00:00"));
        scheduleDTO.setStartDate(Date.valueOf("2023-12-01"));
        scheduleDTO.setEndDate(Date.valueOf("2023-12-31"));
        scheduleDTO.setRecurrence("Daily");
        billDTO.setSchedules(List.of(scheduleDTO));

        Bill bill = new Bill();
        bill.setBill_name("Electricity Bill");
        bill.setBill_amount(100.0);
        bill.setBill_status("Paid");

        when(billService.createFullTask(any(BillDTO.class))).thenReturn(bill);

        String json = objectMapper.writeValueAsString(billDTO);

        mockMvc.perform(post("/bills/createFullBill")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bill_name").value("Electricity Bill"))
                .andExpect(jsonPath("$.bill_amount").value(100.0))
                .andExpect(jsonPath("$.bill_status").value("Paid"));

        verify(billService, times(1)).createFullTask(any(BillDTO.class));
    }



    @Test
    public void testCreateBill_Failure_EmptySchedule() throws Exception {
        BillDTO billDTO = new BillDTO();
        billDTO.setBill_name("Electricity Bill");
        billDTO.setAmount(100.0);
        billDTO.setBill_status("Paid");
        billDTO.setSchedules(null);

        String json = objectMapper.writeValueAsString(billDTO);

        mockMvc.perform(post("/bills/createFullBill")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest()); // Expecting BadRequest

        verify(billService, times(0)).createFullTask(any(BillDTO.class));
    }

    @Test
    public void testGetBillsGroupedByEndDate() throws Exception {
        Bill bill1 = new Bill();
        bill1.setBill_name("Electricity Bill");
        bill1.setImg_path("image1.png");

        Schedule schedule1 = new Schedule();
        schedule1.setEnd_date(Date.valueOf("2025-02-20"));
        schedule1.setDue_time(Time.valueOf("10:00:00"));
        bill1.setSchedules(List.of(schedule1));

        Bill bill2 = new Bill();
        bill2.setBill_name("Water Bill");
        bill2.setImg_path("image2.png");

        Schedule schedule2 = new Schedule();
        schedule2.setEnd_date(Date.valueOf("2025-02-20"));
        schedule2.setDue_time(Time.valueOf("11:00:00"));
        bill2.setSchedules(List.of(schedule2));

        List<Bill> bills = List.of(bill1, bill2);

        when(billRepo.findAll()).thenReturn(bills);

        mockMvc.perform(get("/bills/bills-by-end-date"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['2025-02-20']").isArray())
                .andExpect(jsonPath("$.['2025-02-20'][0].title").value("Electricity Bill"))
                .andExpect(jsonPath("$.['2025-02-20'][1].title").value("Water Bill"));

        verify(billRepo, times(1)).findAll();

    }
}