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

    @OneToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user_id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task; // Link notification to a task

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Date notif_created_date;

    @ManyToMany
    @JoinTable(
            name = "notification_recipients",
            joinColumns = @JoinColumn(name = "notification_id"),
            inverseJoinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
                    @JoinColumn(name = "hive_id", referencedColumnName = "hive_id")
            }
    )
    private Set<HiveMembers> recipients = new HashSet<>();


    // Getters Method
    public Long getNotification_id() {
        return notification_id;
    }

    public Schedule getSchedule_id() {
        return schedule_id;
    }

    public User getUser_id() {
        return user_id;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public Date getNotif_created_date() {
        return notif_created_date;
    }

    public Set<HiveMembers> getRecipients() {
        return recipients;
    }

    public Task getTask() {
        return task;
    }

    // Setters Method
    public void setNotification_id(Long id) {
        this.notification_id = id;
    }

    public void setSchedule_id(Schedule schedule) {
        this.schedule_id = schedule;
    }

    public void setUser_id(User user) {
        this.user_id = user;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNotif_created_date(Date notif_created_date) {
        this.notif_created_date = notif_created_date;
    }

    public void setRecipients(Set<HiveMembers> recipients) {
        this.recipients = recipients;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}