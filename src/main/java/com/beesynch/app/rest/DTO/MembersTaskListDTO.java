package com.beesynch.app.rest.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "img_path", "firstName"
})
public class MembersTaskListDTO {
    private String firstName;
    private String img_path;

    // Constructor that matches the query
    public MembersTaskListDTO(String firstName, String img_path) {
        this.firstName = firstName;
        this.img_path = img_path;
    }

    // Getters and Setters
    public String getFirstname() {
        return firstName;
    }
    public void setFirstname(String firstName) {
        this.firstName = firstName;
    }

    public String getImg_path() {
        return img_path;
    }
    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

}
