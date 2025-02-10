package com.beesynch.app.rest.service;
import com.beesynch.app.rest.DTO.BillDTO;
import com.beesynch.app.rest.DTO.ScheduleDTO;
import com.beesynch.app.rest.Models.*;
import com.beesynch.app.rest.Repo.*;
import com.beesynch.app.rest.Service.BillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillServiceTest {

    @InjectMocks
    private BillService billService;

    @Mock
    private BillRepo billRepo;

    @Mock
    private ScheduleRepo scheduleRepo;

    @Mock
    private HiveRepo hiveRepo;

    @Mock
    private NotificationRepo notificationRepo;

    private Hive mockHive;
    private BillDTO mockBillDTO;

    @BeforeEach
    void setUp() {
        mockHive = new Hive();
        mockHive.setHive_id(1L);

        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setStartDate(Date.valueOf("2025-02-10"));
        scheduleDTO.setEndDate(Date.valueOf("2025-03-10"));
        scheduleDTO.setRecurrence("monthly");
        scheduleDTO.setDueTime(Time.valueOf("18:00:00"));

        mockBillDTO = new BillDTO();
        mockBillDTO.setHive_id(1L);
        mockBillDTO.setBill_name("Electricity");
        mockBillDTO.setAmount(2000.0);
        mockBillDTO.setDescription("Monthly electricity bill");
        mockBillDTO.setBill_status("Pending");
        mockBillDTO.setImg_path("/images/bill.png");
        mockBillDTO.setSchedules(Arrays.asList(scheduleDTO));
    }

    @Test
    void createFullTask_ShouldCreateBillAndSchedule() {
        when(hiveRepo.findById(1L)).thenReturn(Optional.of(mockHive));

        Bill savedBill = new Bill();
        savedBill.setBill_id(1L);
        when(billRepo.save(any(Bill.class))).thenReturn(savedBill);

        Bill result = billService.createFullTask(mockBillDTO);

        assertNotNull(result);
        assertEquals(1L, result.getBill_id());
        verify(billRepo, times(1)).save(any(Bill.class));
        verify(scheduleRepo, times(1)).save(any(Schedule.class));
        verify(notificationRepo, times(1)).save(any(Notification.class));
    }

    @Test
    void createFullTask_ShouldThrowException_WhenHiveNotFound() {
        when(hiveRepo.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            billService.createFullTask(mockBillDTO);
        });

        assertTrue(exception.getMessage().contains("Hive not found with ID: 1"));
        verify(billRepo, never()).save(any(Bill.class));
    }
}
