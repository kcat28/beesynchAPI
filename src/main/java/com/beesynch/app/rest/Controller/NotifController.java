package com.beesynch.app.rest.Controller;

import com.beesynch.app.rest.Models.Notification;
import com.beesynch.app.rest.Repo.NotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notifications")
public class NotifController {

    @Autowired
    private NotificationRepo notificationRepo;

    //get notif by notif id
    @GetMapping("/getbyId/{notifId}")
    public Optional<Notification> getNotificationById(@PathVariable("notifId") Long notifId){
        return notificationRepo.findById(notifId);
    }

    // get notif by user id
    @GetMapping("/getbyUser/{user_id}")
    public List<Notification> user_id(@PathVariable("user_id") Long user_id){
        return notificationRepo.findByUser_id(user_id);
    }

    //get notif by Hive id (those with matching users only)
    @GetMapping("/getbyHive/{hive_id}")
    public List<Notification> hive_id(@PathVariable("hive_id") Long hive_id){
        return notificationRepo.findbyHive(hive_id);
    }

}
