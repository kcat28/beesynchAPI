package com.beesynch.app.rest.Controller;

import com.beesynch.app.rest.DTO.AddMembersRequestDTO;
import com.beesynch.app.rest.DTO.HiveMembersDTO;
import com.beesynch.app.rest.DTO.MembersTaskListDTO;
import com.beesynch.app.rest.Models.User;
import com.beesynch.app.rest.Repo.HiveMembersRepo;
import com.beesynch.app.rest.Repo.HiveRepo;
import com.beesynch.app.rest.Security.JwtUtil;
import com.beesynch.app.rest.Service.HiveService;
import com.beesynch.app.rest.Service.RankingService;
import com.beesynch.app.rest.Repo.UserRepo;
import com.beesynch.app.rest.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/HiveMembers")
public class HiveMembersController {
    @Autowired
    private HiveMembersRepo hiveMembersRepo;

    @Autowired
    private RankingService rankingService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private HiveRepo hiveRepo;

    @Autowired
    private HiveService hiveService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/all")
    public List<HiveMembersDTO> getAllHiveMembers() {
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
    public List<MembersTaskListDTO> getMembersTaskListInfo() {
        return hiveMembersRepo.getMembersTaskListInfo();
    }


    @GetMapping("/CompRateAndPoints") // fetch stats for userId that triggered the request controller
    public Map<String, Double> getCompletionRate(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);

        rankingService.updateLeaderboardWeekly();
        List<Object[]> results = hiveMembersRepo.getCompRateAndPoints(userId);

        if (!results.isEmpty()) {
            Object[] row = results.get(0); // Assuming you only need the first row
            double completionRate = row[0] != null ? ((Number) row[0]).doubleValue() : 0.0;
            double points = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;

            return Map.of("completionRate", completionRate, "points", points);
        }

        return Map.of("completionRate", 0.0, "points", 0.0); // Default values if no data
    }


    @PostMapping("/join")
    public ResponseEntity<String> addMemberToHive(@RequestBody AddMembersRequestDTO request) {
        System.out.println("Received request: " + request);  // Log to check if data is null
        hiveService.addMemberToHive(request.getAdminUserId(), request.getMemberUsername());
        return ResponseEntity.ok("User " + request.getMemberUsername() + " has been added to the hive and notification sent!.");
    }


    @DeleteMapping("/Remove/{userId}")
    public ResponseEntity<?> removeHiveMember(@PathVariable long userId) {
        Long loggedInUserId = userService.getLoggedInUserId();
        if (loggedInUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in.");
        }

        System.out.println("Logged-in user ID: " + loggedInUserId);

        User admin = userRepo.findById(loggedInUserId).orElse(null);
        if (admin == null || !admin.getIsAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the hive master can remove members.");
        }

        if (!hiveMembersRepo.existsByUserId(userId)) {
            return ResponseEntity.notFound().build();
        }

        try {
            hiveMembersRepo.deleteHiveMember(userId);
            return ResponseEntity.ok("Deleted user with user ID: " + userId + " successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing member: " + e.getMessage());
        }
    }
}