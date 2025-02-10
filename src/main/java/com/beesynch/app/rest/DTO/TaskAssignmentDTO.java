package com.beesynch.app.rest.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskAssignmentDTO {
    @JsonProperty("user_id")
    private Long user_id; // ID of the user to assign the task to
    @JsonProperty("assignedDate")
    private Date assignedDate;

    public TaskAssignmentDTO(long l, LocalDate now) {
        this.user_id = l;
        this.assignedDate = Date.valueOf(now);
    }

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