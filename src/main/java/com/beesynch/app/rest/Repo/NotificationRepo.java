package com.beesynch.app.rest.Repo;

import com.beesynch.app.rest.Models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
public interface NotificationRepo extends JpaRepository<Notification, Long>{
}
