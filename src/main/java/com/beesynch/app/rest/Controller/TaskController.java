package com.beesynch.app.rest.Controller;

import com.beesynch.app.rest.DTO.ScheduleDTO;
import com.beesynch.app.rest.DTO.TaskAssignmentDTO;
import com.beesynch.app.rest.DTO.TaskCreationRequestDTO;
import com.beesynch.app.rest.DTO.TaskDTO;
import com.beesynch.app.rest.Models.Schedule;
import com.beesynch.app.rest.Service.TaskService;
import com.beesynch.app.rest.Models.Task;
import com.beesynch.app.rest.Repo.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private TaskService taskService;

    // CREATE a new task
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
        if (request.getSchedules() == null || request.getSchedules().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: schedules must not be empty.");
        }
        System.out.println("Received Schedule: " + request.getSchedules());
        for (ScheduleDTO schedule : request.getSchedules()) {
            System.out.println("Due Time: " + schedule.getDueTime());

            // Normal Validation
            if (schedule.getDueTime() == null) {
                return ResponseEntity.badRequest().body("Error: due_time must not be null in the schedule.");
            }

            if (schedule.getStartDate() == null) {
                return ResponseEntity.badRequest().body("Error: startDate is required in schedule.");
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
    public List<TaskDTO> getAllTasks() {
        return taskRepo.findAll().stream()
                .map(task -> new TaskDTO(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getCategory(),
                        task.getTask_status(),
                        task.getRewardpts(),
                        task.getImg_path(),
                        task.getImgProof(),
                        task.getSchedule().stream()
                                .map(schedule -> new ScheduleDTO(
                                        schedule.getTask().getId(),
                                        schedule.getStart_date(),
                                        schedule.getEnd_date(),
                                        schedule.getRecurrence(),
                                        schedule.getDue_time()))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    // READ all open tasks (tasks with no assignment)
    @GetMapping("/open") // done
    public List<TaskDTO> getOpenTasks() {
        return taskRepo.findUnassignedTasks().stream()
                .map(task -> new TaskDTO(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getCategory(),
                        task.getTask_status(),
                        task.getRewardpts(),
                        task.getImg_path(),
                        task.getImgProof(),
                        task.getSchedule().stream()
                                .map(schedule -> new ScheduleDTO(
                                        schedule.getTask().getId(),
                                        schedule.getStart_date(),
                                        schedule.getEnd_date(),
                                        schedule.getRecurrence(),
                                        schedule.getDue_time()))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());

    }

    @GetMapping("/tasks-by-end-date")
    public Map<String, List<Map<String, String>>> getTasksGroupedByEndDate() {
        return taskRepo.findAll().stream()
                .flatMap(task -> task.getSchedule().stream() // Flatten the schedules
                        .map(schedule -> Map.entry(
                                schedule.getEnd_date() != null ? schedule.getEnd_date().toString() : "No End Date",
                                Map.of(
                                        "title", task.getTitle(),
                                        "img_path", task.getImg_path() != null ? task.getImg_path() : "",
                                        "due_time",
                                        schedule.getDue_time() != null ? schedule.getDue_time().toString()
                                                : "No Due Time"))))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey, // Group by end date
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList()) // Map values
                ));
    }

    // for joyce popup
    @GetMapping("/get_byId/{taskId}")
    public Optional<TaskDTO> getTaskById(@PathVariable Long taskId) {
        return taskRepo.findById(taskId)
                .map(task -> new TaskDTO(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getCategory(),
                        task.getTask_status(),
                        task.getRewardpts(),
                        task.getImg_path(),
                        task.getImgProof(),
                        task.getSchedule().stream()
                                .map(schedule -> new ScheduleDTO(
                                        schedule.getTask().getId(),
                                        schedule.getStart_date(),
                                        schedule.getEnd_date(),
                                        schedule.getRecurrence(),
                                        schedule.getDue_time()))
                                .collect(Collectors.toList())));
    }

    // for nicole userId's Tasks
    @GetMapping("/get_byUserId/{user_id}")
    public List<Map<String, Object>> findTasksByUserId(@PathVariable Long user_id) {
        return taskRepo.findTasksByUserId(user_id).stream()
                .map(Task -> {
                    Map<String, Object> taskMap = new HashMap<>();
                    taskMap.put("id", Task.getId());
                    taskMap.put("title", Task.getTitle());
                    taskMap.put("description", Task.getDescription());
                    taskMap.put("category", Task.getCategory());
                    taskMap.put("task_status", Task.getTask_status());
                    taskMap.put("rewardpts", Task.getRewardpts());
                    taskMap.put("img_path", Task.getImg_path());
                    taskMap.put("imgProof", Task.getImgProof());
                    taskMap.put("due_time", Task.getSchedule().stream()
                            .map(schedule -> schedule.getDue_time())
                            .findFirst()
                            .orElse(null));
                    return taskMap;
                })
                .collect(Collectors.toList());
    }

    // Get missed tasks for a user
    @GetMapping("/missed/{user_id}")
    public List<Map<String, Object>> findMissedTasksByUserId(@PathVariable Long user_id) {
        return taskRepo.findMissedTasksByUserId(user_id).stream()
                .map(Task -> {
                    Map<String, Object> taskMap = new HashMap<>();
                    taskMap.put("id", Task.getId());
                    taskMap.put("title", Task.getTitle());
                    taskMap.put("description", Task.getDescription());
                    taskMap.put("category", Task.getCategory());
                    taskMap.put("task_status", Task.getTask_status());
                    taskMap.put("rewardpts", Task.getRewardpts());
                    taskMap.put("img_path", Task.getImg_path());
                    taskMap.put("imgProof", Task.getImgProof());
                    taskMap.put("due_time", Task.getSchedule().stream()
                            .map(Schedule::getDue_time)
                            .findFirst()
                            .orElse(null));
                    return taskMap;
                })
                .collect(Collectors.toList());
    }

    // update task
    @PutMapping("/updateTask")
    public ResponseEntity<?> ediTask(@RequestBody TaskCreationRequestDTO request) {
        try {
            Task task = taskService.editTask(request);
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // markAsComplete
    @PutMapping("/markAsComplete/{taskId}")
    public ResponseEntity<?> markTaskAsComplete(@PathVariable Long taskId) {
        try {
            taskRepo.markTaskAsCompleted(taskId);
            return ResponseEntity.ok().body("Task Marked as Complete");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Update task proof image
    @PutMapping("/updateImgProof/{taskId}")
    public ResponseEntity<?> updateTaskProofImage(@PathVariable Long taskId, @RequestBody Map<String, String> payload) {
        try {
            String imgProof = payload.get("imgProof");
            if (imgProof == null || imgProof.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: imgProof must not be null or empty.");
            }

            Task task = taskRepo.findById(taskId)
                    .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

            task.setImgProof(imgProof);
            taskRepo.save(task);

            return ResponseEntity.ok().body("Task proof image updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // markAsMissed
    @PutMapping("/markAsMissed/{taskId}")
    public ResponseEntity<?> markTaskAsMissed(@PathVariable Long taskId) {
        try {
            taskRepo.markTaskAsMissed(taskId);
            return ResponseEntity.ok().body("Task Marked as Missed");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete a task
    @DeleteMapping("delete/{taskId}") // done
    public String deleteTask(@PathVariable Long taskId) {
        taskRepo.deleteById(taskId);
        return "Task deleted with ID: " + taskId;
    }

    // controller is for manual flushing only/ automated every 00:00 monday ph time
    @GetMapping("/flushCompleted")
    public String manuallyFlushCompletedTasks() {
        Integer deletedCount = taskService.flushCompletedTasks();
        return deletedCount + " Completed tasks deleted.";
    }

}
