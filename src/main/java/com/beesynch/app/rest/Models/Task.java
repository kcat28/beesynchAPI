package com.beesynch.app.rest.Models;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 200)
    private String description;

    @Column(length = 100)
    private String category;

    @Column(nullable = false)
    private String task_status;

    private Date completion_date;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TaskAssignment> assignments = new ArrayList<>();

    // Getters Method
    public Long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription(){
        return description;
    }
    public String getCategory(){
        return category;
    }
    public String getTask_status(){
        return task_status;
    }
    public Date getCompletion_date(){
        return completion_date;
    }
    public List<TaskAssignment> getAssignments(){
        return assignments;
    }

    // Setters Method
    public void setId(Long id) {
        this.id = id;
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
    public void setCompletion_date(Date completion_date) {
        this.completion_date = completion_date;
    }
    public void setAssignments(List<TaskAssignment> assignments) {
        this.assignments = assignments;
    }

}


