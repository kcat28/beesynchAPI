package com.beesynch.app.rest.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "hiveId", "hiveName", "userId", "img_path", "userEmail", "userName", "firstName", "lastName",
          "role", "points", "achievements"
})
public class HiveMembersDTO {
    private Long userId;
    private String img_path;
    private String userName;
    private String firstName;
    private String lastName;
    private String userEmail;
    private Long hiveId;
    private String hiveName;
    private String role;
    private int points;

    // Constructor
    public HiveMembersDTO(Long userId, String img_path, String userName, String firstName, String lastName,
                          String userEmail, Long hiveId, String hiveName, String role,
                          int points) {
        this.userId = userId;
        this.img_path = img_path;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userEmail = userEmail;
        this.hiveId = hiveId;
        this.hiveName = hiveName;
        this.role = role;
        this.points = points;
    }

    public HiveMembersDTO() {

    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getUserName() { //
        return userName;
    }

    public void setUserName(String userName) { //
        this.userName = userName;
    }

    public String getFirstName() { //
        return firstName;
    }

    public void setFirstName(String firstName) { //
        this.firstName = firstName;
    }

    public String getLastName() { //
        return lastName;
    }

    public void setLastName(String lastName) { //
        this.lastName = lastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getHiveId() {
        return hiveId;
    }

    public void setHiveId(Long hiveId) {
        this.hiveId = hiveId;
    }

    public String getHiveName() {
        return hiveName;
    }

    public void setHiveName(String hiveName) {
        this.hiveName = hiveName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}
