package com.beesynch.app.rest.Controller;

import com.beesynch.app.rest.DTO.HiveMembersDTO;
import com.beesynch.app.rest.DTO.MembersTaskListDTO;
import com.beesynch.app.rest.Models.User;
import com.beesynch.app.rest.Repo.HiveMembersRepo;
<<<<<<< HEAD
import com.beesynch.app.rest.Service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
=======
import com.beesynch.app.rest.Repo.UserRepo;
import com.beesynch.app.rest.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
>>>>>>> 03ad2a9 (Modified remove member function based on the logged in user id)
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/HiveMembers")
public class HiveMembersController {
    @Autowired
    private HiveMembersRepo hiveMembersRepo;

    @Autowired
<<<<<<< HEAD
    private RankingService rankingService;
=======
    private UserRepo userRepo;

    @Autowired
    private UserService userService;
>>>>>>> 03ad2a9 (Modified remove member function based on the logged in user id)

    @GetMapping("/")
    public List<HiveMembersDTO> getAllHiveMembers(){
        return hiveMembersRepo.getAllHiveMembers();
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<HiveMembersDTO> getHiveMemberByUsername(@PathVariable String username) {
        HiveMembersDTO memberDTO = hiveMembersRepo.findByUsername(username);
        if (memberDTO != null) {
            return ResponseEntity.ok().body(memberDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/membersInfo")
    public List<MembersTaskListDTO> getMembersTaskListInfo(){
        return hiveMembersRepo.getMembersTaskListInfo();
    }


    @GetMapping("/CompletionRate/{id}")
    public Double getCompletionRate(@PathVariable Long id) {
        rankingService.updateCompletionRates();
        return hiveMembersRepo.getCompletionRate(id);
    }


    // add member to a hive db
    @PostMapping("/join")
    public String newHiveMate(@RequestBody HiveMembersDTO hiveMate) {
        // Save new hive mate to database
        // hiveService.save(hiveMate);
        return "HiveMate added and notification sent!";
    }



>>>>>>> 03ad2a9 (Modified remove member function based on the logged in user id)
    @DeleteMapping("/Remove/{userId}")
    public ResponseEntity<?> removeHiveMember(@PathVariable long userId) {

        Long loggedInUserId = userService.getLoggedInUserId();
        if (loggedInUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in.");
        }

        System.out.println("Logged-in user ID: " + loggedInUserId);

        User admin = userRepo.findById(loggedInUserId).orElse(null);
        if (admin == null || !admin.getIsAdmin()) { // Check if the user is not an admin
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the hive master can remove members.");
        }

        if (!hiveMembersRepo.existsByUserId(userId)) {
            return ResponseEntity.notFound().build();
        }

        try {
            hiveMembersRepo.deleteHiveMember(userId);
<<<<<<< HEAD
            return ResponseEntity.ok().body("removed user id " + userId + " from hive successfully");
=======
            return ResponseEntity.ok("Deleted successfully") ;
>>>>>>> 03ad2a9 (Modified remove member function based on the logged in user id)
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing member: " + e.getMessage());
        }
    }
<<<<<<< HEAD



=======
>>>>>>> 03ad2a9 (Modified remove member function based on the logged in user id)
}
