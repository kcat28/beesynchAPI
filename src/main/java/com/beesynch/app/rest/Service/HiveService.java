package com.beesynch.app.rest.Service;


import com.beesynch.app.rest.Models.Hive;
import com.beesynch.app.rest.DTO.HiveDTO;
import com.beesynch.app.rest.Models.User;
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

    public Hive createHiveNAdmin (HiveDTO hiveDTO) {
        if (hiveRepo.existsByHiveName(hiveDTO.getHiveName())) {
            throw new RuntimeException("Hive " + hiveDTO.getHiveName() +" already exists");
        }

        //step 1 create hive
        Hive hive = new Hive();
        hive.setHive_id(hiveDTO.getHiveId());
        hive.setHiveName(hiveDTO.getHiveName());
        hive.setHive_created_date(Date.valueOf(java.time.LocalDate.now()));
        hive.setImg_path(hive.getImg_path());

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
        userRepo.changeToAdmin(user.getId());
        return hiveRepo.save(hive);
    }


//    public Hive deletHive (HiveDTO hiveDTO) {
//
//    }
}
