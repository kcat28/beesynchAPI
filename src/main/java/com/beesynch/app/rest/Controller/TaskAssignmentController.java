package com.beesynch.app.rest.Controller;

import com.beesynch.app.rest.Models.TaskAssignment;
import com.beesynch.app.rest.Repo.TaskAssignmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks/taskAssignment")
    public class TaskAssignmentController {

    @Autowired
    private TaskAssignmentRepo taskAssignmentRepo;


    // GET all tasks assigned to a specific user
    @GetMapping("/user/{userId}")
    public List<TaskAssignment> getTasksForUser(@PathVariable Long userId){
        return taskAssignmentRepo.findByUserId(userId);
    }

    // UNASSIGN a task (delete task assignment entry)
    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<String> unassignTask(@PathVariable Long assignmentId) {
        if (!taskAssignmentRepo.existsById(assignmentId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Assignment ID not found");
        }
        taskAssignmentRepo.deleteById(assignmentId);
        return ResponseEntity.ok("Task unassigned with assignment ID: " + assignmentId);
    }




}
