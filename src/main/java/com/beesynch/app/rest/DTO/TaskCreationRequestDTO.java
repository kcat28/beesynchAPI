package com.beesynch.app.rest.DTO;

import java.util.List;

public class TaskCreationRequestDTO {
    private Long task_id;
    private String title;
    private String description;
    private String category;
    private String task_status;
    private Integer rewardpts;
    private String img_path;
    private String imgProof;
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

    public Integer getRewardpts() {
        return rewardpts;
    }

    public String getImg_path() {
        return img_path;
    }

    public String getImgProof() {
        return imgProof;
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

    public void setImgProof(String imgProof) {
        this.imgProof = imgProof;
    }

    public void setSchedules(List<ScheduleDTO> schedules) {
        this.schedules = schedules;
    }

    public void setAssignments(List<TaskAssignmentDTO> assignments) {
        this.assignments = assignments;
    }

    public Long getTask_id() {
        return task_id;
    }

    public void setTask_id(Long task_id) {
        this.task_id = task_id;
    }
}