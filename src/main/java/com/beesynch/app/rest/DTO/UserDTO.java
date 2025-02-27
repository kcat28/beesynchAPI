package com.beesynch.app.rest.DTO;

//this will be for login and change password purposes

import jakarta.persistence.Transient;

public class UserDTO {
    private String user_name;
    private String user_password;

    @Transient // Ensures these aren't persisted in the database
    private String currentPassword;

    @Transient
    private String newPassword;

    private String recovery_code;

    private String security_answers;

    // Getters and Setters
    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRecovery_code() {
        return recovery_code;
    }

    public void setRecovery_code(String recovery_code) {
        this.recovery_code = recovery_code;
    }

    public String getSecurity_answers() {
        return security_answers;
    }

    public void setSecurity_answers(String security_answers) {
        this.security_answers = security_answers;
    }
}
