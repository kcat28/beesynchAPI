package com.beesynch.app.rest.Repo;
import  com.beesynch.app.rest.Models.Hive;
import com.beesynch.app.rest.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HiveRepo extends JpaRepository<Hive, Long>{
    boolean existsByHiveName(String hiveName);

    @Query("SELECT h FROM Hive h WHERE h.createdBy = :adminUser")
    Optional<Hive> findByCreatedBy(@Param("adminUser") User adminUser);
}
