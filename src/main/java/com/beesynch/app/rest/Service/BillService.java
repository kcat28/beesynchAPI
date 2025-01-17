package com.beesynch.app.rest.Service;


import com.beesynch.app.rest.DTO.BillDTO;
import com.beesynch.app.rest.DTO.ScheduleDTO;
import com.beesynch.app.rest.Models.*;
import com.beesynch.app.rest.Repo.BillRepo;
import com.beesynch.app.rest.Repo.HiveRepo;
import com.beesynch.app.rest.Repo.NotificationRepo;
import com.beesynch.app.rest.Repo.ScheduleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class BillService {

    @Autowired
    private BillRepo billRepo;

    @Autowired
    private ScheduleRepo scheduleRepo;

    @Autowired
    private HiveRepo hiveRepo;

    @Autowired
    private NotificationRepo notificationRepo;

    public Bill createFullTask(BillDTO billCreationRequest) {

        // step 1: create and save the bill
        try {
            // Fetch the Hive object using hive_id
            Hive hive = hiveRepo.findById(billCreationRequest.getHive_id())
                    .orElseThrow(() -> new RuntimeException("Hive not found with ID: " + billCreationRequest.getHive_id()));

            // create bill and set hive obj
            Bill bill = new Bill();
            bill.setHive_id(hive);  // Set the full Hive object instead of just the hive_id
            bill.setBill_name(billCreationRequest.getBill_name());
            bill.setBill_amount(billCreationRequest.getAmount());
            bill.setDescription(billCreationRequest.getDescription());
            bill.setBill_status(billCreationRequest.getBill_status());

            // save the bill to the repository(sql)
            Bill savedBill = billRepo.save(bill);
            if (savedBill.getBill_id() == null) {
                throw new RuntimeException("Error: Bill was not properly saved or its ID is null.");
            }

            // step 2: save schedule
            if (billCreationRequest.getSchedules() != null && !billCreationRequest.getSchedules().isEmpty()) {
                for (ScheduleDTO scheduleDTO : billCreationRequest.getSchedules()) {
                    Schedule schedule = new Schedule();
                    schedule.setBill_id(savedBill);  // Set the saved Bill to the Schedule

                    System.out.println("Creating Schedule: " + schedule);

                    schedule.setStart_date(scheduleDTO.getStartDate());
                    schedule.setEnd_date(scheduleDTO.getEndDate());
                    schedule.setRecurrence(scheduleDTO.getRecurrence());
                    schedule.setDue_time(scheduleDTO.getDueTime());
                    scheduleRepo.save(schedule);  // Save the schedule

                    Notification notification = new Notification();
                    notification.setSchedule_id(schedule);
                    notification.setMessage("New Bill Created: " + savedBill.getBill_name());
                    notification.setNotif_created_date(new java.sql.Date(System.currentTimeMillis()));
                    notificationRepo.save(notification);
                }
            }

            return savedBill;  // return the saved bill object

        } catch (Exception e) {
            // Log the error and throw an exception
            e.printStackTrace();
            throw new RuntimeException("Error creating the task: " + e.getMessage());
        }
    }

    public Map<String, List<String>> getBillsGroupedByEndDate() {
        List<Object[]> results = billRepo.findBillDetails(); // Fetch from repository

        return results.stream()
                .collect(Collectors.groupingBy(
                        result -> formatDateOnly((Date) result[1]), // Group by formatted end_date (index 1)
                        Collectors.mapping(result -> formatBillDetails(result), Collectors.toList()) // Format bill details
                ));
    }

    // Helper to extract and format date for grouping
    private String formatDateOnly(Date date) {
        if (date == null) {
            return "Unknown Date"; // Handle null values
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // yyyy-MM-dd format
        return dateFormat.format(date);
    }

    // Helper to format bill details into a readable string
    private String formatBillDetails(Object[] result) {
        String billName = (String) result[0]; // Bill name (index 0)
        Date dueTime = (Date) result[2]; // Due time (index 2)

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss"); // HH:mm:ss format

        String formattedDueTime = (dueTime != null) ? timeFormat.format(dueTime) : "Unknown Due Time";

        // Combine bill details into a formatted string
        return billName + " : " + formattedDueTime;
    }

}
