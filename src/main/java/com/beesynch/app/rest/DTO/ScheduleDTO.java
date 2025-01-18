package com.beesynch.app.rest.DTO;

import java.sql.Date;
import java.sql.Time;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleDTO {

    private Long task;
    private Long bill_id;
    private Long user_id;
    private Date start_date;
    private Date end_date;
    private String recurrence;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @JsonProperty("due_time")  // Ensure this matches the JSON property name
    private Time due_time;

    // Getters and setters
    public Long getTask(){return task;}
    public Long setTask(){return task;}
    public Long getBill_id(){return bill_id;}
    public void setBill_id(Long bill_id){this.bill_id = bill_id;}
    public Long getUser_id() {
        return user_id;
    }
    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
    public Date getStartDate() {
        return start_date;
    }
    @JsonProperty("start_date")
    public void setStartDate(Date startDate) {
        this.start_date = startDate;
    }
    @JsonProperty("end_date")
    public Date getEndDate() {
        return end_date;
    }
    public void setEndDate(Date endDate) {
        this.end_date = endDate;
    }
    public String getRecurrence() {
        return recurrence;
    }
    public void setRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }
    public Time getDueTime() {
        return due_time;
    }
    public void setDueTime(Time dueTime) {
        this.due_time = dueTime;
    }
}