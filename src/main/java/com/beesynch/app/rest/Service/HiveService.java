package com.beesynch.app.rest.Service;


import com.beesynch.app.rest.Models.*;
import com.beesynch.app.rest.DTO.HiveDTO;
import com.beesynch.app.rest.Repo.HiveMembersRepo;
import com.beesynch.app.rest.Repo.HiveRepo;
import com.beesynch.app.rest.Repo.RankingRepo;
import com.beesynch.app.rest.Repo.UserRepo;
import com.beesynch.app.rest.Security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Optional;

@Service
@Transactional
public class HiveService {

    @Autowired
    HiveRepo hiveRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RankingRepo rankingRepo;

    @Autowired
    HiveMembersRepo hiveMembersRepo;

    public Hive createHiveNAdmin (HiveDTO hiveDTO, HttpServletRequest request) {
        if (hiveRepo.existsByHiveName(hiveDTO.getHiveName())) {
            System.out.println("Hive " + hiveDTO.getHiveName() +" already exists");
            throw new RuntimeException("Hive " + hiveDTO.getHiveName() +" already exists");
        }

        // extract token from auth header
        String token = request.getHeader("Authorization").substring(7); // remove "Bearer"
        String userId = String.valueOf(jwtUtil.extractUserId(token)); // extract user id from jwt

        //step 1 create hive
        Hive hive = new Hive();
        hive.setHive_id(hiveDTO.getHiveId());
        hive.setHiveName(hiveDTO.getHiveName());
        hive.setHive_created_date(Date.valueOf(java.time.LocalDate.now()));
        hive.setImg_path(hiveDTO.getImg_path());
        hiveRepo.save(hive);

        //step 2 set user admin
        Optional<User> optionalUser = userRepo.findById(Long.valueOf(userId));

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        //optional to obj
        User user = optionalUser.get();
        if (user.getIsAdmin()){
            System.out.println("Warning: User is already Admin");
        }
        userRepo.changeToAdmin(user.getId());


        //step 3 make id join hivemembers table
        HiveMembers newMember = new HiveMembers();
        HiveMemberId id = new HiveMemberId(user.getId(), hive.getHive_id());
        Ranking newRankData = new Ranking();

        newRankData.setUser_id(user);
        newRankData.setHive_id(hive);
        newRankData.setRank_position(0);
        newRankData.setPeriod_start(new java.sql.Date(System.currentTimeMillis()));
        newRankData.setPeriod_end(null);
        rankingRepo.save(newRankData);

        newMember.setId(id);
        newMember.setUser_id(user);
        newMember.setHive_id(hive);
        newMember.setRanking_id(newRankData);
        newMember.setRole("ROLE_ADMIN");
        newMember.setPoints(0);
        newMember.setAchievements(null);
        newMember.setcompletionRate(0.0);
        hiveMembersRepo.save(newMember);

        return hiveRepo.save(hive);
    }



    public Hive updateHive(HiveDTO hiveDTO) {
        //step 1 fetch hive by id
        Hive existingHive = hiveRepo.findById(hiveDTO.getHiveId())
                .orElseThrow(() -> new RuntimeException("Hive not found with ID " + hiveDTO.getHiveId()));

        existingHive.setHiveName(hiveDTO.getHiveName());
        if(hiveDTO.getImg_path() != null){
            existingHive.setImg_path(hiveDTO.getImg_path());
        }

        return hiveRepo.save(existingHive);

    }
}
