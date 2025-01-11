package com.beesynch.app.rest.Controller;


import com.beesynch.app.rest.Models.Notification;
import com.beesynch.app.rest.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/notifications")
public class NotifController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public Notification sendNotification(@RequestParam String message, @RequestParam Long taskId, @RequestParam Long userId, @RequestParam(required = false) Set<Long> householdMemberIds) {
        return notificationService.sendNotification(message, taskId, userId, householdMemberIds);
    }
}
