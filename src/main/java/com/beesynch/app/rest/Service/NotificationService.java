package com.beesynch.app.rest.Service;

import com.beesynch.app.rest.Models.*;
import com.beesynch.app.rest.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private TaskRepo taskRepo; // Repository to fetch task details

    @Autowired
    private HiveMembersRepo householdMemberRepo;

    public Notification sendNotification(String message, Long taskId, Long userId, Set<Long> householdMemberIds) {
        // Fetch the related task
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + taskId));

        // Create a new Notification
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setTask(task); // Link the notification to the Task
        notification.setNotif_created_date(new java.sql.Date(System.currentTimeMillis()));

        // Assign household members as recipients if provided
        Set<HiveMembers> recipients = new HashSet<>();
        if (householdMemberIds != null && !householdMemberIds.isEmpty()) {
            for (Long memberId : householdMemberIds) {
                householdMemberRepo.findById(memberId).ifPresent(recipients::add);
            }
        }
        notification.setRecipients(recipients);

        // Set the notification's status based on the Task's category
        notification.setStatus(Optional.ofNullable(task.getCategory()).orElse("undefined")); // Use default if NULL

        // Save and return the notification
        return notificationRepo.save(notification);
    }
}