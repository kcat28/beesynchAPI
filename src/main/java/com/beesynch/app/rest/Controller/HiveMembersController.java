package com.beesynch.app.rest.Controller;

import com.beesynch.app.rest.DTO.HiveMembersDTO;
import com.beesynch.app.rest.DTO.MembersTaskListDTO;
import com.beesynch.app.rest.Repo.HiveMembersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/HiveMembers")
public class HiveMembersController {
    @Autowired
    private HiveMembersRepo hiveMembersRepo;

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


    // add member to a hive db
    @PostMapping("/join")
    public String newHiveMate(@RequestBody HiveMembersDTO hiveMate) {
        // Save new hive mate to database
        // hiveService.save(hiveMate);

        return "HiveMate added and notification sent!";
    }

}
