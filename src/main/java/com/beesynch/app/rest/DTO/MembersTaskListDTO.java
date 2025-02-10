package com.beesynch.app.rest.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "userId", "img_path", "firstName",
})
public class MembersTaskListDTO {
    private Long userId;
    private String userName;
    private String img_path;


    // Constructor that matches the query
    public MembersTaskListDTO(Long userId, String userName, String img_path) {
        this.userId = userId;
        this.userName = userName;
        this.img_path = img_path;
    }

    public MembersTaskListDTO() {

    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return userName;
    }
    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getImg_path() {
        return img_path;
    }
    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

}
