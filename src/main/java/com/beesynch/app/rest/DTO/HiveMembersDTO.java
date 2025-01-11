package com.beesynch.app.rest.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Collections;
import java.util.List;

@JsonPropertyOrder({
        "userId", "userName", "firstName", "lastName", "userEmail", "hiveId", "hiveName", "role", "points", "achievements"
})
public class HiveMembersDTO {
    private Long userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String userEmail;
    private Long hiveId;
    private String hiveName;
    private String role;
    private int points;
    private String achievements;

    // Constructor that matches the query
    public HiveMembersDTO(Long userId, String userName, String firstName, String lastName,
                          String userEmail, Long hiveId, String hiveName, String role,
                          int points, String achievements) {
        this.userId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userEmail = userEmail;
        this.hiveId = hiveId;
        this.hiveName = hiveName;
        this.role = role;
        this.points = points;
        this.achievements = achievements;
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

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getFirstname() {
        return firstName;
    }

    public void setFirstname(String firstname) {
        this.firstName = firstname;
    }

    public String getLastname() {
        return lastName;
    }

    public void setLastname(String lastname) {
        this.lastName = lastname;
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

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public List<String> getAchievements() {
        return Collections.singletonList(achievements);
    }

    public void setAchievements(List<String> achievements) {
        this.achievements = String.valueOf(achievements);
    }
}
