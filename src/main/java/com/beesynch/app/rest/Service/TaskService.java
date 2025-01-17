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
}
