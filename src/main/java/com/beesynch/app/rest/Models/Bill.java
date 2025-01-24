package com.beesynch.app.rest.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bills")
@JsonPropertyOrder({"hive_id", "schedule", "bill_id", "bill_name", "bill_status", "bill_description", "bill_amount", "img_path"})
public class Bill {

    @ManyToOne
    @JoinColumn(name = "hive_id", nullable = false)
    private Hive hive_id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bill_id;

    @Column(length = 100)
    String bill_name;

    @Column(nullable = false)
    Double amount;

    @Column(length = 200)
    private String description;

    @Column(nullable = false)
    String bill_status;

    @Column
    String img_path;

    @OneToMany(mappedBy = "bill_id", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Schedule> schedule = new ArrayList<>();

    // Getters Method
    public Hive getHive_id(){
        return hive_id;
    }
    public List<Schedule> getSchedule(){
        return schedule;
    } //01/19/2025

    public Long getBill_id(){
        return bill_id;
    }
    public String getBill_name(){
        return bill_name;
    }
    public Double getBill_amount(){
        return amount;
    }
    public String getDescription(){return description;}
    public String getBill_status(){
        return bill_status;
    }

    public String getImg_path(){
        return img_path;
    }



    // Setters Method
    public void setBill_id(Long id) {
        this.bill_id = id;
    }
    public void setHive_id(Hive id){this.hive_id = id;}
    public void setBill_name(String bill_name) {
        this.bill_name = bill_name;
    }
    public void setBill_amount(Double bill_amount) {
        this.amount = bill_amount;
    }
    public void setDescription(String description) {this.description = description;}
    public void setBill_status(String bill_status) {
        this.bill_status = bill_status;
    }
    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }
    public void setSchedules(List<Schedule> schedules){
        this.schedule = schedules;
    } //01/19/2025


}
