    package com.beesynch.app.rest.Controller;

    import com.beesynch.app.rest.Models.Notification;
    import com.beesynch.app.rest.Repo.NotificationRepo;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.Optional;

    @RestController
    @RequestMapping("/notifications")
    public class NotifController {

        @Autowired
        private NotificationRepo notificationRepo;

        //get notif by notif id // CHECKED JAN 30 2025 - JEP
        @GetMapping("/getbyId/{notifId}")
        public ResponseEntity<Notification> getNotificationById(@PathVariable("notifId") Long notifId) {
            Optional<Notification> notification = notificationRepo.findById(notifId);
            if (notification.isPresent()) {
                return ResponseEntity.ok(notification.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }


        // get notif by user id // CHECKED JAN 30 2025 - JEP
        @GetMapping("/getbyUser/{user_id}")
        public List<Notification> getNotificationsByUserId(@PathVariable("user_id") Long user_id){
            List<Notification> notifications = notificationRepo.findByUser_id(user_id);
            System.out.println("Fetched Notifications: " + notifications);
            return notifications;
        }

        //get notif by Hive id (those with matching users only)
        @GetMapping("/getbyHive/{hive_id}") // CHECKED JAN 30 2025 - JEP
        public List<Notification> getNotificationsByHiveId(@PathVariable("hive_id") Long hive_id){
            List<Notification> notifications = notificationRepo.findbyHive(hive_id);
            System.out.println("Fetched Notifications: " + notifications);
            return notifications;
        }

    }
