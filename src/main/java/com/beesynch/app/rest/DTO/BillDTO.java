package com.beesynch.app.rest.DTO;

import com.beesynch.app.rest.Models.Hive;

import java.util.List;

public class BillDTO {

    private Long bill_id;
    private Long hive_id;
    private String bill_name;
    private Double amount;
    private String bill_status;
    private String description;
    private List<ScheduleDTO> schedules;
    private String img_path;

    public void setHive_id(Long hive_id){
        this.hive_id = hive_id;
    }
    public Long getHive_id(){
        return hive_id;
    }

    public void setBill_name(String bill_name) {
        this.bill_name = bill_name;
    }
    public String getBill_name() {
        return bill_name;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public Double getAmount() {
        return amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {return description;}

    public void setBill_status(String bill_status) {
        this.bill_status = bill_status;
    }
    public String getBill_status() {
        return bill_status;
    }

    public void setSchedules(List<ScheduleDTO> schedules) {
        this.schedules = schedules;
    }
    public List<ScheduleDTO> getSchedules() {
        return schedules;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }
    public String getImg_path() {
        return img_path;
    }
    public Long getBill_id() {
        return bill_id;
    }
    public void setBill_id(Long id) {
        this.bill_id = id;
    }

}
