package com.beesynch.app.rest.DTO;
import com.beesynch.app.rest.Models.Notification;

import java.util.List;
public class TaskCreationRequestDTO {
    private String title;
    private String description;
    private String category;
    private String task_status;
    private Integer rewardpts;
    private String img_path;
    private List<ScheduleDTO> schedules; // Optional schedule details
    private List<TaskAssignmentDTO> assignments; // Optional assignments


    // Getters and setters
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getCategory() {
        return category;
    }
    public String getTask_status() {
        return task_status;
    }
    public Integer getRewardpts(){
        return rewardpts;
    }
    public String getImg_path() {
        return img_path;
    }
    public List<ScheduleDTO> getSchedules() {
        return schedules;
    }
    public List<TaskAssignmentDTO> getAssignments() {
        return assignments;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setTask_status(String task_status) {
        this.task_status = task_status;
    }
    public void setRewardpts(Integer rewardpts) {
        this.rewardpts = rewardpts;
    }
    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }
    public void setSchedules(List<ScheduleDTO> schedules) {
        this.schedules = schedules;
    }
    public void setAssignments(List<TaskAssignmentDTO> assignments) {
        this.assignments = assignments;
    }

}