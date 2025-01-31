package com.beesynch.app.rest.Repo;

import com.beesynch.app.rest.Models.Hive;
import com.beesynch.app.rest.Models.Ranking;
import com.beesynch.app.rest.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RankingRepo extends JpaRepository<Ranking, Long>{

    @Query("SELECT a.user.id, SUM(t.rewardpts), h.hive.hive_id " +
            "FROM TaskAssignment a " +
            "JOIN a.task t " +
            "JOIN a.user u " +
            "JOIN HiveMembers h ON h.user.id = u.id " +
            "WHERE t.task_status = 'Completed' " +
            "GROUP BY a.user.id, h.hive")
    List<Object[]> getLeaderboard();

}
