package com.beesynch.app.rest.Repo;

import com.beesynch.app.rest.Models.Schedule;
import com.beesynch.app.rest.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ScheduleRepo extends JpaRepository<Schedule, Long>{

    @Modifying
    @Query("DELETE FROM Schedule s WHERE s.task.id = :taskid")
    void deleteByTaskId(@Param("taskid") Long taskid);

    @Modifying
    @Query("DELETE FROM Schedule s WHERE s.bill_id.bill_id = :billid")
    void deleteByTaskIdbill(@Param("billid") Long billid);
}
