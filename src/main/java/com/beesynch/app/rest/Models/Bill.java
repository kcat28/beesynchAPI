package com.beesynch.app.rest.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bill_id;

    @ManyToOne
    @JoinColumn(name = "hive_id", nullable = false)
    private Hive hive_id;

    @Column(length = 100)
    String bill_name;

    @Column(nullable = false)
    Double amount;

    @Column(length = 200)
    private String description;

    @Column(nullable = false)
    String bill_status;

    // Getters Method
    public Long getBill_id(){
        return bill_id;
    }
    public Hive getHive_id(){
        return hive_id;
    }
    public String getBill_name(){
        return bill_name;
    }
    public Double getBill_amount(){
        return amount;
    }
    public String getDescription(){return description;
    }
    public String getBill_status(){
        return bill_status;
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
    public void setDescription(String description) {
        this.description = description;
    }
    public void setBill_status(String bill_status) {
        this.bill_status = bill_status;
    }
}
