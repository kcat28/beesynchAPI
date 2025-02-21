package com.beesynch.app.rest.Service;

import com.beesynch.app.rest.DTO.RankingDTO;
import com.beesynch.app.rest.Models.Hive;
import com.beesynch.app.rest.Models.HiveMembers;
import com.beesynch.app.rest.Models.Ranking;
import com.beesynch.app.rest.Models.User;
import com.beesynch.app.rest.Repo.HiveMembersRepo;
import com.beesynch.app.rest.Repo.HiveRepo;
import com.beesynch.app.rest.Repo.RankingRepo;
import com.beesynch.app.rest.Repo.UserRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class RankingService {

    @Autowired
    private RankingRepo rankingRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private HiveRepo hiveRepo;

    @Autowired
    private HiveMembersRepo hiveMembersRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Scheduled(cron = "0 0 16 * * MON", zone = "Asia/Manila") // update
    public void updateLeaderboardWeekly() {
        deleteLeaderBoard();
        updateLeaderBoard();
    }

    // Method to update leaderboard
    public void updateLeaderBoard() {

        // step 1 & 2: get leaderboard data
        List<Object[]> leaderboardRaw = rankingRepo.getLeaderboard();

        // step 3: sort leaderboard by total points (descending order)
        leaderboardRaw.sort((a, b) -> ((Long) b[1]).compareTo((Long) a[1]));

        int rank = 1;
        Long prevPoints = null;
        int position = 0;

        for (Object[] obj : leaderboardRaw){
            Long userId = (Long) obj[0];  // Get user ID
            Long totalPoints = (Long) obj[1];  // Get the total reward points
            Long hiveId = (Long) obj[2];  // Get the hive name

            position++;

            // assign rank if the total points are different
            if (!totalPoints.equals(prevPoints)) {
                rank = position;
            }
            prevPoints = totalPoints;

            // create a new Ranking object
            Ranking ranking = new Ranking();
            User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            Hive hive = hiveRepo.findById(hiveId).orElseThrow(() -> new RuntimeException("Hive not found"));
            LocalDate endOfWeek = LocalDate.now().with(DayOfWeek.SUNDAY);

            ranking.setUser_id(user);
            ranking.setHive_id(hive);
            ranking.setRank_position(rank);
            ranking.setPeriod_start(new Date(System.currentTimeMillis()));
            ranking.setPeriod_end(Date.valueOf(endOfWeek));

            rankingRepo.save(ranking);

            // debugging purposes: payload
            System.out.println("Rank: " + (1));
            System.out.println("User ID: " + userId);
            System.out.println("Total Points: " + totalPoints);
            System.out.println("HiveId:  " + hiveId);
        }
    }
    public void deleteLeaderBoard(){
        rankingRepo.deleteAll();
        entityManager.flush(); // changes are flushed to the database
        entityManager.clear(); // clears persistence context to avoid stale data
        System.out.println("Current rankings cleared.");
    }

    @Scheduled(cron = "0 0 16 * * MON", zone = "Asia/Manila") // Every Monday at 4PM
    public void updateCompletionRates() {
        List<HiveMembers> members = hiveMembersRepo.findAll();

        for (HiveMembers member : members) {
            Long userId = member.getUser_id().getId();
            Long completed = hiveMembersRepo.countCompletedTasksForUser(userId);
            Long allTasks = hiveMembersRepo.totalCountTasksForUser(userId);

            double completionRate = (allTasks == 0) ? 0.0 : (completed.doubleValue() / allTasks.doubleValue()) * 100;

            member.setcompletionRate(completionRate);
            hiveMembersRepo.save(member);
        }

        System.out.println("Completion rates updated successfully!");
    }
}
