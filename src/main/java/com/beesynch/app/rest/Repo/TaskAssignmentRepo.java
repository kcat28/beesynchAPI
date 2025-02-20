package com.beesynch.app.rest.Repo;

import com.beesynch.app.rest.Models.TaskAssignment;
import com.beesynch.app.rest.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskAssignmentRepo extends JpaRepository<TaskAssignment, Long>{
    List<TaskAssignment> findByUserId(Long user_id);

    @Modifying
    @Query("DELETE FROM Schedule s WHERE s.task.id = :taskId")
    void deleteByTaskId(Long taskId);
}