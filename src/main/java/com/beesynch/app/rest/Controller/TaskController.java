package com.beesynch.app.rest.Controller;


import com.beesynch.app.rest.DTO.ScheduleDTO;
import com.beesynch.app.rest.DTO.TaskAssignmentDTO;
import com.beesynch.app.rest.DTO.TaskCreationRequestDTO;
import com.beesynch.app.rest.Service.TaskService;
import com.beesynch.app.rest.Models.Task;
import com.beesynch.app.rest.Repo.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.stream.Collectors;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
    public class TaskController {

        @Autowired
        private TaskRepo taskRepo;

        @Autowired
        private TaskService taskService;

        // CREATE a new task (unassigned)
        @PostMapping("/createFullTask") // done
        public ResponseEntity<?> createFullTask(@RequestBody TaskCreationRequestDTO request) {
            System.out.println("Assignments in request: " + request.getAssignments());
            if (request.getAssignments() != null) {
                for (TaskAssignmentDTO assignment : request.getAssignments()) {
                    System.out.println("Assignment: user_id=" + assignment.getId()
                            + ", assignedDate=" + assignment.getAssignedDate());
                }
            }
            // Validate required schedule fields (if schedule is provided)
            System.out.println("Incoming payload: " + request);
            System.out.println("Received Schedule: " + request.getSchedules());
            if (request.getAssignments() != null) {
                for (TaskAssignmentDTO assignment : request.getAssignments()) {
                    System.out.println("Assignment: " + assignment);
                }
            }
            if (request.getSchedules() != null) {
                for(ScheduleDTO schedule : request.getSchedules() ){
                    System.out.println("Due Time: " + schedule.getDueTime());

                    // Normal Validation
                    if (request.getSchedules() == null || request.getSchedules().isEmpty()) {
                        return ResponseEntity.badRequest().body("Error: schedules must not be empty.");
                    }

                    if (schedule.getDueTime() == null) {
                        return ResponseEntity.badRequest().body("Error: due_time must not be null in the schedule.");
                    }

                    if (schedule.getStartDate() == null ) { // removed: || schedule.getEndDate() == null
                        return ResponseEntity.badRequest().body("Error: startDate is required in schedule.");
                    }
                }
            }

            try {
                Task task = taskService.createFullTask(request);
                return ResponseEntity.ok(task);
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

        // READ all tasks
        @GetMapping("/all-tasks") // done
        public List<Task> getAllTasks() {
            return taskRepo.findAll();
        }

        // READ all open tasks (tasks with no assignment)
        @GetMapping("/open") // done
        public List<Task> getOpenTasks() {
            return taskRepo.findUnassignedTasks();
        }

        //UPDATE (task fields)
//        @PutMapping("/{taskId}")
//        public Task updateTask(@PathVariable Long taskId, @RequestBody Task taskDetails) {
//            Task task = taskRepo.findById(taskId)
//                    .orElseThrow(() -> new RuntimeException("Task not found."));
//            task.setTitle(taskDetails.getTitle());
//            task.setDescription(taskDetails.getDescription());
//            task.setCategory(taskDetails.getCategory());
//            task.setTask_status(taskDetails.getTask_status());
//            return taskRepo.save(task);
//        }

        //01/19/2025 - for calendar view
        @GetMapping("/tasks-by-end-date")
        public Map<String, List<String>> getTasksGroupedByEndDate() {
            return taskService.getTasksGroupedByEndDate();
        }

        //Delete a task
        @DeleteMapping("/{taskId}") // done
        public String deleteTask(@PathVariable Long taskId) {
            taskRepo.deleteById(taskId);
            return "Task deleted with ID: " + taskId;
        }
}
