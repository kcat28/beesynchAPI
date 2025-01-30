package com.beesynch.app.rest.Repo;

import com.beesynch.app.rest.Models.Notification;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification, Long>{

    @Query("SELECT n FROM Notification n WHERE n.user.id = :user_id")
    List<Notification> findByUser_id(@Param("user_id") Long user_id);


    @Query("SELECT n FROM Notification n WHERE n.user.id IN (SELECT hm.user.id FROM HiveMembers hm WHERE hm.hive.hive_id = :hive_id)")
    List<Notification> findbyHive(@Param("hive_id") Long hive_id);
}
