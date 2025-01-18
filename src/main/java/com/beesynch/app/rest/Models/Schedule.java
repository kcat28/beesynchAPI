package com.beesynch.app.rest.Models;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schedule_id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    @JsonBackReference
    private Task task;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user_id;

    @Column(nullable = false)
    private Date start_date;

    @Column()
    private Date end_date;
    private String recurrence; // subject for study
    private Time due_time;

    // Getters Method
    public Long getSchedule_id(){
        return schedule_id;
    }
    public Task getTask(){
        return task;
    }
    public Bill getBill_id(){
        return bill_id;
    }
    public User getUser_id(){
        return user_id;
    }
    public Date getStart_date(){
        return start_date;
    }
    public Date getEnd_date(){
        return end_date;
    }
    public String getRecurrence(){
        return recurrence;
    }
    public Time getDue_time(){
        return due_time;
    }

    // Setters Method
    public void setSchedule_id(Long id){
        this.schedule_id = id;
    }
    public void setTask(Task task){
        this.task = task;
    }
    public void setBill_id(Bill bill){
        this.bill_id = bill_id;
    }
    public void setUser_id(User user){
        this.user_id = user;
    }
    public void setStart_date(Date start_date){
        this.start_date = start_date;
    }
    public void setEnd_date(Date end_date){
        this.end_date = end_date;
    }
    public void setRecurrence(String recurrence){
        this.recurrence = recurrence;
    }
    public void setDue_time(Time due_time){
        this.due_time = due_time;
    }
}
