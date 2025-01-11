package com.beesynch.app.rest.Repo;

import com.beesynch.app.rest.Models.TaskAssignment;
import com.beesynch.app.rest.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskAssignmentRepo extends JpaRepository<TaskAssignment, Long>{
    List<TaskAssignment> findByUserId(Long user_id);
}