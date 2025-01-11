package com.beesynch.app.rest.Models;
import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "ranking")
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ranking_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user_id;

    @ManyToOne
    @JoinColumn(name = "hive_id", nullable = false)
    private Hive hive_id;

    @Column(nullable = false)
    private Integer rank_position;

    @Column(nullable = false)
    private Date period_start;

    @Column(nullable = false)
    private Date period_end;

    // Getters Method
    public Long getRanking_id(){
        return ranking_id;
    }
    public User getUser_id(){
        return user_id;
    }
    public Hive getHive_id(){
        return hive_id;
    }
    public Integer getRank_position(){
        return rank_position;
    }
    public Date getPeriod_start(){
        return period_start;
    }
    public Date getPeriod_end(){
        return period_end;
    }

    //Setters Method
    public void setRanking_id(Long id){
        this.ranking_id = id;
    }
    public void setUser_id(User user){
        this.user_id = user;
    }
    public void setHive_id(Hive hive){
        this.hive_id = hive;
    }
    public void setRank_position(Integer rank_position){
        this.rank_position = rank_position;
    }
    public void setPeriod_start(Date period_start){
        this.period_start = period_start;
    }
    public void setPeriod_end(Date period_end){
        this.period_end = period_end;
    }

}
