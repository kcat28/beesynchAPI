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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
        Optional<User> optionalUser = userRepo.findById(Long.valueOf(userId));
        User user = optionalUser.get();


        //step 1 create hive
        Hive hive = new Hive();
        hive.setHive_id(hiveDTO.getHiveId());
        hive.setHiveName(hiveDTO.getHiveName());
        hive.setHive_created_date(Date.valueOf(java.time.LocalDate.now()));
        hive.setImg_path(hiveDTO.getImg_path());
        hive.setCreatedBy(user);
        hiveRepo.save(hive);


        //step 2 set user admin
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        //optional to obj
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

        public void addMemberToHive(Long adminUserId, String memberUsername, Long memberId) {
            // Ensure adminUserId is always provided
            if (adminUserId == null) {
                throw new IllegalArgumentException("Admin user ID must be provided.");
            }

            // Ensure at least one of memberUsername or memberId is provided
            if ((memberUsername == null || memberUsername.isBlank()) && memberId == null) {
                throw new IllegalArgumentException("Either member username or member ID must be provided.");
            }

        // Step 1: Find user
        User adminUser = userRepo.findById(adminUserId)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + adminUserId));
        System.out.println("Found user: " + adminUser.getUser_name());


        // Step 2: Checks if the user is really an admin or a member
        Boolean isAdmin = userRepo.findIsAdminByUserId(adminUserId);
        if (Boolean.FALSE.equals(isAdmin)) {
            throw new RuntimeException("User with ID " + adminUserId + " is not an admin.");
        }


        // Step 3: Finds the Hive Managed by the Admin user
        Hive hive = hiveRepo.findByCreatedBy(adminUser)
                .orElseThrow(() -> new RuntimeException("No hive found for this admin."));
        System.out.println("Found hive: " + hive.getHiveName());


        // Step: 4 Finds the user to be Added through their username
        User memberUser = userRepo.findByUserName(memberUsername);
        if(memberUser == null){
            memberUser = userRepo.findById(memberId).orElse(null); // Retrieve User from Optional
        }

        if (memberUser == null) {
            throw new RuntimeException("User not found.");
        }
        System.out.println("Found member user: " + memberUser.getUser_name());


        // Step 5: Checks if the found user is already a member of the admin user's hive
        boolean isMember = hiveMembersRepo.existsByUserIdAndHiveId(memberUser.getId(), hive.getHive_id());
        if (isMember) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is already a member of this hive.");
        }



        // Step 6: Creates the new member's entry and then saved to the database
        // Ranking
        Ranking rank = new Ranking();
        rank.setUser_id(memberUser);
        rank.setHive_id(hive);
        rank.setRank_position(0);
        rank.setPeriod_start(new java.sql.Date(System.currentTimeMillis()));
        rank.setPeriod_end(null);
        rankingRepo.save(rank);

        // Hive Member
        HiveMembers newMember = new HiveMembers();
        HiveMemberId id = new HiveMemberId(memberUser.getId(), hive.getHive_id());
        newMember.setHive_id(hive);
        newMember.setUser_id(memberUser);
        newMember.setId(id);
        newMember.setRanking_id(rank);
        newMember.setRole("Member");
        newMember.setPoints(0);
        hiveMembersRepo.save(newMember);

        System.out.println("User added successfully.");
    }
}
