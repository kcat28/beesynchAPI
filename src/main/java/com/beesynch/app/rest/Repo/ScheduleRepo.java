package com.beesynch.app.rest.Repo;

import com.beesynch.app.rest.Models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ScheduleRepo extends JpaRepository<Schedule, Long>{
}
