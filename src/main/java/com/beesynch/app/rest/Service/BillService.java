package com.beesynch.app.rest.Service;


import com.beesynch.app.rest.DTO.BillDTO;
import com.beesynch.app.rest.DTO.ScheduleDTO;
import com.beesynch.app.rest.Models.*;
import com.beesynch.app.rest.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    @Autowired
    private UserRepo userRepo;

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
            bill.setImg_path(billCreationRequest.getImg_path());

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

    public Bill editBill(BillDTO billUpdateRequest) {
        // step 1 fetch existing bill by id
        Bill existingBill = billRepo.findById(billUpdateRequest.getBill_id())
                .orElseThrow(() -> new RuntimeException("Bill not found with ID: " + billUpdateRequest.getBill_id()));

        Optional<Hive> hive = hiveRepo.findById(billUpdateRequest.getHive_id());
        //step 2 update fetched bill

        existingBill.setBill_name(billUpdateRequest.getBill_name());
        if (hive.isPresent()) {
            existingBill.setHive_id(hive.get());
        } else {
            throw new RuntimeException("Hive not found with ID: " + billUpdateRequest.getHive_id());
        }
        existingBill.setBill_amount(billUpdateRequest.getAmount());
        existingBill.setDescription(billUpdateRequest.getDescription());
        existingBill.setBill_status(billUpdateRequest.getBill_status());
        if(billUpdateRequest.getImg_path() != null) {
            existingBill.setImg_path(billUpdateRequest.getImg_path());
        }
        Bill savedBill = billRepo.save(existingBill);

        //step 3 update schedule if available
        if(billUpdateRequest.getSchedules() != null && !billUpdateRequest.getSchedules().isEmpty()) {
            scheduleRepo.deleteByTaskIdbill(savedBill.getBill_id()); //remove old schedules
            for(ScheduleDTO scheduleDTO : billUpdateRequest.getSchedules()) {
                Schedule schedule = new Schedule();
                schedule.setBill_id(savedBill);
                schedule.setStart_date(scheduleDTO.getStartDate());
                schedule.setEnd_date(scheduleDTO.getEndDate());
                schedule.setRecurrence(scheduleDTO.getRecurrence());
                schedule.setDue_time(scheduleDTO.getDueTime());

                User user = scheduleDTO.getUser_id() != null ?
                        userRepo.findById(scheduleDTO.getUser_id()).orElse(null) : null;
                schedule.setUser_id(user);

                scheduleRepo.save(schedule);
            }
        }
        return savedBill;
    }
}
