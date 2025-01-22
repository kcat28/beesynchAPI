package com.beesynch.app.rest.DTO;

import java.util.List;

public class TaskDTO {

    private Long task_id;
    private String title;
    private String description;
    private String category;
    private String task_status;
    private Integer rewardpts;
    private String img_path;
    private List<ScheduleDTO> schedules;  // List of schedules

    public TaskDTO(Long task_id, String title, String description, String category, String task_status, Integer rewardpts, String img_path, List<ScheduleDTO> schedules) {
        this.task_id = task_id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.task_status = task_status;
        this.rewardpts = rewardpts;
        this.img_path = img_path;
        this.schedules = schedules;
    }

    // Getters and Setters
    public Long getTask_id() {
        return task_id;
    }

    public void setTask_id(Long task_id) {
        this.task_id = task_id;
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTask_status() {
        return task_status;
    }

    public void setTask_status(String task_status) {
        this.task_status = task_status;
    }

    public Integer getRewardpts() {
        return rewardpts;
    }

    public void setRewardpts(Integer rewardpts) {
        this.rewardpts = rewardpts;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public List<ScheduleDTO> getSchedules() {
        return schedules;
    }

}
