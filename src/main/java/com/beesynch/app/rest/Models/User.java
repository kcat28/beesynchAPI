package com.beesynch.app.rest.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Column(nullable = false)
    private String first_name;

    @Column(nullable = false)
    private String last_name;

    @Column(nullable = false)
    private String user_name;

    @Column(nullable = false, unique = true)
    private String user_email;

    @Column(nullable = false, unique = true)
    // @JsonIgnore
    private String user_password;

    @Column
    private String img_path;

    @Column
    private String recovery_code;

    @Column(nullable = false)
    private Boolean is_admin = false;

    @Column(columnDefinition = "TEXT")
    private String security_answers;

    // Getter methods
    public long getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public String getImg_path() {
        return img_path;
    }

    public String getRecovery_code() {
        return recovery_code;
    }

    public Boolean getIsAdmin() {
        return is_admin;
    }

    public String getSecurity_answers() {
        return security_answers;
    }

    // Setter methods
    public void setId(long id) {
        this.id = id;
    }

    public void setFirst_name(String firstName) {
        this.first_name = firstName;
    }

    public void setLast_name(String lastName) {
        this.last_name = lastName;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public void setRecovery_code(String recovery_code) {
        this.recovery_code = recovery_code;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.is_admin = isAdmin;
    }

    public void setSecurity_answers(String security_answers) {
        this.security_answers = security_answers;
    }
}
