package com.beesynch.app.rest.Controller;

import com.beesynch.app.rest.Models.TaskAssignment;
import com.beesynch.app.rest.Repo.TaskAssignmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
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

    //update? not sure pa

    // UNASSIGN a task (delete task assignment entry)
    @DeleteMapping("/{assignmentId}")
    public String unassignTask(@PathVariable Long assignmentId) {
        taskAssignmentRepo.deleteById(assignmentId);
        return "Task unassigned with assignment ID: " + assignmentId;
    }

}
