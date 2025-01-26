package com.beesynch.app.rest.Models;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "hive")
public class Hive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hive_id;
    @Column(name = "hive_name",unique = true, length = 100, nullable = false)
    String hiveName;
    @Column(nullable = false)
    Date hive_created_date;
    @Column(name = "img_path")
    private String img_path;

    // Getters Method
    public Long getHive_id(){
        return hive_id;
    }
    public String getHiveName(){
        return hiveName;
    }
    public Date getHive_created_date(){
        return hive_created_date;
    }
    public String getImg_path() {
        return img_path;
    }

    // Setters Method
    public void setHive_id(Long id){
        this.hive_id = id;
    }
    public void setHiveName(String hive_name){
        this.hiveName = hive_name;
    }
    public void setHive_created_date(Date hive_created_date){
        this.hive_created_date = hive_created_date;
    }
    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }
}
