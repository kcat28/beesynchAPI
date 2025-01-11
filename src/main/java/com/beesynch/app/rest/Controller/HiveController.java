package com.beesynch.app.rest.Controller;
import com.beesynch.app.rest.Models.Hive;
import com.beesynch.app.rest.Repo.HiveRepo;
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

    @GetMapping//done
    public List<Hive> getAllHives() {
        return hiveRepo.findAll();
    }

    @PostMapping//done
    public ResponseEntity<String> createHive(@RequestParam String name) {
        // Check if hive name exists
        if (hiveRepo.existsByHiveName(name)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Hive with name: " + name + " already exists.");
        }

        // save new hive instance
        Hive hive = new Hive();
        hive.setHiveName(name);
        hive.setHive_created_date(new java.sql.Date(System.currentTimeMillis()));

        hiveRepo.save(hive);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Hive created with name: " + name + " and creation date: " + hive.getHive_created_date());
    }

    @DeleteMapping()//done
    public ResponseEntity<String> deleteHive(@RequestParam String hiveId) {
        hiveRepo.deleteById(hiveId);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Hive deleted with ID: " + hiveId);
    }
}
