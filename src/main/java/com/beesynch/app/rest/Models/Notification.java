package com.beesynch.app.rest.Models;

import jakarta.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notification_id;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String message;

    @Column
    private Date notif_created_date;


    // Getters Method
    public Long getNotification_id() {
        return notification_id;
    }

    public Schedule getSchedule_id() {
        return schedule;
    }

    public User getUser_id() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public Date getNotif_created_date() {
        return notif_created_date;
    }


    // Setters Method
    public void setSchedule_id(Schedule schedule) {
        this.schedule = schedule;
    }

    public void setUser_id(User user) {
        this.user = user;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNotif_created_date(Date notif_created_date) {
        this.notif_created_date = notif_created_date;
    }

}