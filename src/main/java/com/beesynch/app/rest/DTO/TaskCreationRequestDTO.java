package com.beesynch.app.rest.DTO;
import java.util.List;
public class TaskCreationRequestDTO {
    private String title;
    private String description;
    private String category;
    private String task_status;
    private ScheduleDTO schedule; // Optional schedule details
    private List<TaskAssignmentDTO> assignments; // Optional assignments

    // Getters and setters
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getTask_status() {
        return task_status;
    }
    public void setTask_status(String task_status) {
        this.task_status = task_status;
    }
    public ScheduleDTO getSchedule() {
        return schedule;
    }
    public void setSchedule(ScheduleDTO schedule) {
        this.schedule = schedule;
    }
    public List<TaskAssignmentDTO> getAssignments() {
        return assignments;
    }
    public void setAssignments(List<TaskAssignmentDTO> assignments) {
        this.assignments = assignments;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}