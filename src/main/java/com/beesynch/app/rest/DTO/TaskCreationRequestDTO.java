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


}