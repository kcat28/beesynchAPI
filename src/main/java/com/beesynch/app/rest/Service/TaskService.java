package com.beesynch.app.rest.Service;

import com.beesynch.app.rest.DTO.ScheduleDTO;
import com.beesynch.app.rest.DTO.TaskAssignmentDTO;
import com.beesynch.app.rest.DTO.TaskCreationRequestDTO;

import com.beesynch.app.rest.Models.*;

import com.beesynch.app.rest.Repo.*;

import org.aspectj.weaver.ast.Not;
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
public class TaskService {

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private ScheduleRepo scheduleRepo;

    @Autowired
    private TaskAssignmentRepo taskAssignmentRepo;

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private UserRepo userRepo; // assuming it exists to fetch users by ID

    public Task createFullTask(TaskCreationRequestDTO taskCreationRequest) {
        try {
            // Step 1: Create and save the Task
            Task task = new Task();
            task.setTitle(taskCreationRequest.getTitle());
            task.setDescription(taskCreationRequest.getDescription()); // i think this allows for null
            task.setCategory(taskCreationRequest.getCategory());
            task.setTask_status(taskCreationRequest.getTask_status());
            task.setRewardpts(taskCreationRequest.getRewardpts());
            System.out.println("Image Path: " + taskCreationRequest.getImg_path());
            task.setImg_path(taskCreationRequest.getImg_path());
            Task savedTask = taskRepo.save(task);
            if (savedTask.getId() == null) {
                throw new RuntimeException("Error: Task was not properly saved or its ID is null.");
            }

            // Step 2: Create and save the Schedule. notifications are tied
            if (taskCreationRequest.getSchedules() != null && !taskCreationRequest.getSchedules().isEmpty()) {

                for(ScheduleDTO scheduleDTO : taskCreationRequest.getSchedules()){
                    Schedule schedule = new Schedule();
                    schedule.setTask(savedTask);

                    if (savedTask.getId() == null) {
                        throw new RuntimeException("Error: Task was not properly saved or its ID is null.");
                    }
                    schedule.setStart_date(scheduleDTO.getStartDate());
                    schedule.setEnd_date(scheduleDTO.getEndDate());
                    schedule.setRecurrence(scheduleDTO.getRecurrence());
                    schedule.setDue_time(scheduleDTO.getDueTime());

                    // Fetch the User and assign to Schedule
                    User user = null;

                    // Check if user_id is not null
                    if(scheduleDTO.getUser_id() != null){
                        user = userRepo.findById(scheduleDTO.getUser_id())
                                .orElseThrow(() -> new RuntimeException("User not found with ID: " + scheduleDTO.getUser_id()));
                    }
                    // Allow user to remain null if user_id is null
                    schedule.setUser_id(user);
                    scheduleRepo.save(schedule);

                    // Create notification for this schedule
                        Notification notification = new Notification();
                        notification.setSchedule_id(schedule);
                        notification.setUser_id(user);
                        notification.setMessage("New task Created: " + savedTask.getTitle());
                        notification.setNotif_created_date(new java.sql.Date(System.currentTimeMillis()));
                        notificationRepo.save(notification);


                }
            }

            // Step 3: Create and save Task Assignments, if provided
            if (taskCreationRequest.getAssignments() != null && !taskCreationRequest.getAssignments().isEmpty()) {

                for (TaskAssignmentDTO assignmentDTO : taskCreationRequest.getAssignments()) {
                    if (assignmentDTO.getId() == null) {
                        System.out.println("Warning: user_id is null for TaskAssignmentDTO, but allowing null as requested.");
                    }
                    if (assignmentDTO.getAssignedDate() == null) {
                        throw new IllegalArgumentException("Error: assignedDate in TaskAssignmentDTO must not be null.");
                    }
                    // Fetch User entity (either null or value)
                    User user = null;
                    if (assignmentDTO.getId() != null) {
                        user = userRepo.findById(assignmentDTO.getId())
                                .orElseThrow(() -> new RuntimeException("User not found with ID: " + assignmentDTO.getId()));
                    }
                    //Create taskAssignment
                    TaskAssignment assignment = new TaskAssignment();
                    assignment.setTask(savedTask);
                    assignment.setUser(user); // User should already be fetched from userRepo
                    assignment.setAssignedDate(assignmentDTO.getAssignedDate());
                    taskAssignmentRepo.save(assignment);

                }
            }

            return savedTask;

        } catch (Exception e) {
            // Log and re-throw for now
            e.printStackTrace();
            throw new RuntimeException("Error creating the task: " + e.getMessage());
        }
    }

    // 01/19/2025 - logic for calendar view display of task
    public Map<String, List<String>> getTasksGroupedByEndDate() {
        List<Object[]> results = taskRepo.findTaskDetails(); // Get raw results

        // Group by end_date (formatted as yyyy-MM-dd) and include title + due_time in the result
        return results.stream()
                .collect(Collectors.groupingBy(
                        result -> formatDateOnly((Date) result[1]), // Group by the formatted end_date (index 1)
                        Collectors.mapping(result -> formatTaskDetails(result), Collectors.toList()) // Format task details
                ));
    }

    // Helper function to extract the date portion for grouping
    private String formatDateOnly(Date date) {
        if (date == null) {
            return "Unknown Date"; // Handle null values
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Date format (yyyy-MM-dd)
        return dateFormat.format(date);
    }

    // Helper function to format the task details
    private String formatTaskDetails(Object[] result) {
        String taskName = (String) result[0]; // Task name (index 0)
        Date dueTime = (Date) result[2]; // Due time (index 2)

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss"); // Time format

        String formattedDueTime = (dueTime != null) ? timeFormat.format(dueTime) : "Unknown Due Time";

        // Combine details into a readable string
        return taskName + " : " + formattedDueTime;
    }
}


