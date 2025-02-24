package com.beesynch.app.rest.Controller;
import com.beesynch.app.rest.DTO.HiveDTO;
import com.beesynch.app.rest.Models.Hive;
import com.beesynch.app.rest.Repo.HiveRepo;
import com.beesynch.app.rest.Service.HiveService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/hive")
public class HiveController {

    @Autowired
    private HiveRepo hiveRepo;

    @Autowired
    private HiveService hiveService;

    @GetMapping("/getAllHives")//done
    public List<Hive> getAllHives() {
        return hiveRepo.findAll();
    }

    @PostMapping("/createHive")
    public ResponseEntity<?> createHive(@RequestBody HiveDTO hiveRequest, HttpServletRequest request) {
        try{
            Hive hive = hiveService.createHiveNAdmin(hiveRequest, request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Hive created with name: " + hive.getHiveName() + " and creation date: " + hive.getHive_created_date());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @DeleteMapping("deleteHive/{hiveId}")//done
    public ResponseEntity<String> deleteHive(@PathVariable Long hiveId) {
        hiveRepo.deleteById(hiveId);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Hive deleted with ID: " + hiveId);
    }

    //edit hive
    @PutMapping("/updateHive")
    public ResponseEntity<?> updateHive(@RequestBody HiveDTO hiveRequest) {
        try{
            Hive hive = hiveService.updateHive(hiveRequest);
            return ResponseEntity.ok("Hive updated successfully " + hive.getHiveName());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
