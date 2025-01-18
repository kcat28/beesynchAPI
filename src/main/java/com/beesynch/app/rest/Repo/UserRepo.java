package com.beesynch.app.rest.Repo;

import com.beesynch.app.rest.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{
    @Query("SELECT u FROM User u WHERE u.user_name = ?1")
    User findByUserName(String userName);

}
