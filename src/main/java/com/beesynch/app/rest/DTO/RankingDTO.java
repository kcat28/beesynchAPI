package com.beesynch.app.rest.DTO;

import java.util.Date;

public class RankingDTO {
    private Long ranking_id;
    private Long user_id;
    private Long hive_id;
    private Integer rank_position;
    private Date period_start;
    private Date period_end;

    public RankingDTO(Long ranking_id,Long user_id, Long hive_id, Integer rank_position, Date period_start, Date period_end) {
        this.ranking_id = ranking_id;
        this.user_id = user_id;
        this.hive_id = hive_id;
        this.rank_position = rank_position;
        this.period_start = period_start;
        this.period_end = period_end;
    }

    //getters and setters

    public Long getRanking_id() {
        return ranking_id;
    }

    public Long getHive_id(){
        return hive_id;
    }

    public Long getUser_id(){
        return user_id;
    }
    public Integer getRank_position() {
        return rank_position;
    }

    public Date getPeriod_start() {
        return period_start;
    }

    public Date getPeriod_end() {
        return period_end;
    }

}
