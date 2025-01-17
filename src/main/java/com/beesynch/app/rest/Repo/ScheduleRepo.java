package com.beesynch.app.rest.Repo;

import com.beesynch.app.rest.Models.Schedule;
import com.beesynch.app.rest.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ScheduleRepo extends JpaRepository<Schedule, Long>{
}
