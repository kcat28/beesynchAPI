package com.beesynch.app.rest.Repo;

import com.beesynch.app.rest.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{
     @Query("SELECT u FROM User u WHERE u.user_name = :userName")
    User findByUserName(@Param("userName") String userName);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.is_admin = TRUE WHERE u.id = :userId")
    void changeToAdmin(@Param("userId")Long userId);

    @Query("SELECT u.is_admin FROM User u WHERE u.id = :userId")
    Boolean findIsAdminByUserId(@Param("userId") Long userId);
}
