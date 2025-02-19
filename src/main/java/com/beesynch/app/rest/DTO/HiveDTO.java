package com.beesynch.app.rest.DTO;

import java.util.Date;

public class HiveDTO {

    public Long hive_id;
    public String hiveName;
    public Date hive_created_date;
    public String img_path;
    public Long user_id;


    public void setHiveId(Long hiveId) {
        this.hive_id = hiveId;
    }
    public void setHiveName(String hiveName) {
        this.hiveName = hiveName;
    }
    public void setHiveCreatedDate(Date hiveCreateDate) {
        this.hive_created_date = hiveCreateDate;
    }
    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }
    public void setUserid(int userid) {
        this.user_id = (long) userid;
    }
    public Long getHiveId() {
        return hive_id;
    }
    public String getHiveName() {
        return hiveName;
    }
    public Date getHiveCreateDate() {
        return hive_created_date;
    }
    public String getImg_path() {
        return img_path;
    }
    public long getUserid() {
        return user_id;
    }
}
