package com.beesynch.app.rest.Repo;

import com.beesynch.app.rest.DTO.TaskCreationRequestDTO;
import com.beesynch.app.rest.Models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task, Long>{

    @Query("SELECT t FROM Task t LEFT JOIN TaskAssignment ta ON t.id = ta.task.id WHERE ta.user IS NULL")
    List<Task> findUnassignedTasks();

    @Query("SELECT t FROM Task t JOIN t.assignments ta WHERE ta.user.id = :user_id")
    List<Task> findTasksByUserId(@Param("user_id") Long userId);


}
