package com.beesynch.app.rest.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "household_members")
public class HiveMembers {

    @EmbeddedId
    private HiveMemberId id;  // Embedded composite primary key

    @ManyToOne
    @MapsId("userId") // This ensures that the foreign key user_id is part of the composite key
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("hiveId")
    @JoinColumn(name = "hive_id", nullable = false)
    private Hive hive;

    @ManyToOne
    @JoinColumn(name = "ranking_id")
    private Ranking ranking;

    @Column(length = 200)
    private String role;

    @Column
    private Integer points;

    @Column
    private Double completionRate = 0.0;

    // Getters and setters
    public HiveMemberId getId() {
        return id;
    }

    public void setId(HiveMemberId id) {
        this.id = id;
    }

    public User getUser_id() {
        return user;
    }

    public void setUser_id(User user_id) {
        this.user = user_id;
    }

    public Hive getHive_id() {
        return hive;
    }

    public void setHive_id(Hive hive_id) {
        this.hive = hive_id;
    }

    public void setcompletionRate(Double completionRate) {
        this.completionRate = completionRate;
    }

    public Ranking getRanking_id() {
        return ranking;
    }

    public void setRanking_id(Ranking ranking_id) {
        this.ranking = ranking_id;
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

}
