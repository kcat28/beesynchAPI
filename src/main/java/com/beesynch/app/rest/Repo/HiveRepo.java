package com.beesynch.app.rest.Repo;
import  com.beesynch.app.rest.Models.Hive;
import org.springframework.data.jpa.repository.JpaRepository;
public interface HiveRepo extends JpaRepository<Hive, Long>{
    boolean existsByHiveName(String hiveName);
}
