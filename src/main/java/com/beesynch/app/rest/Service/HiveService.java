package com.beesynch.app.rest.Service;


import com.beesynch.app.rest.Models.Hive;
import com.beesynch.app.rest.DTO.HiveDTO;
import com.beesynch.app.rest.Models.HiveMemberId;
import com.beesynch.app.rest.Models.HiveMembers;
import com.beesynch.app.rest.Models.User;
import com.beesynch.app.rest.Repo.HiveMembersRepo;
import com.beesynch.app.rest.Repo.HiveRepo;
import com.beesynch.app.rest.Repo.UserRepo;
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
    HiveMembersRepo hiveMembersRepo;

    public Hive createHiveNAdmin (HiveDTO hiveDTO) {
        if (hiveRepo.existsByHiveName(hiveDTO.getHiveName())) {
            System.out.println("Hive " + hiveDTO.getHiveName() +" already exists");
            throw new RuntimeException("Hive " + hiveDTO.getHiveName() +" already exists");
        }

        //step 1 create hive
        Hive hive = new Hive();
        hive.setHive_id(hiveDTO.getHiveId());
        hive.setHiveName(hiveDTO.getHiveName());
        hive.setHive_created_date(Date.valueOf(java.time.LocalDate.now()));
        hive.setImg_path(hiveDTO.getImg_path());

        //step 2 set user admin
        Optional<User> optionalUser = userRepo.findById(hiveDTO.getUserid());

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        //optional to obj
        User user = optionalUser.get();
        if (user.getIsAdmin()){
            System.out.println("Warning: User is already Admin");
        }

        //step 3 make id join hivemembers table

        userRepo.changeToAdmin(user.getId());
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
