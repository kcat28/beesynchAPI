package com.beesynch.app.rest.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;

public class TaskAssignmentDTO {
    @JsonProperty("user_id")
    private Long user_id; // ID of the user to assign the task to
    @JsonProperty("assignedDate")
    private Date assignedDate;

    // Getters and setters
    public Long getId() {
        return user_id;
    }
    public void setId(Long id) {
        this.user_id = id;
    }
    public Date getAssignedDate() {
        return assignedDate;
    }
    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }

}