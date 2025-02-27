package com.beesynch.app.rest.Repo;

import com.beesynch.app.rest.DTO.TaskCreationRequestDTO;
import com.beesynch.app.rest.Models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.List;

public interface TaskRepo extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t LEFT JOIN TaskAssignment ta ON t.id = ta.task.id WHERE ta.user IS NULL")
    List<Task> findUnassignedTasks();

    @Query("SELECT t FROM Task t JOIN t.assignments ta WHERE ta.user.id = :user_id")
    List<Task> findTasksByUserId(@Param("user_id") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Task t WHERE t.task_status = :status")
    Integer flushTaskByStatus(@Param("status") String status);

    @Modifying
    @Transactional
    @Query(value = "UPDATE task SET task_status = 'Completed', completion_date = UTC_TIMESTAMP WHERE task_id = :taskId", nativeQuery = true)
    void markTaskAsCompleted(@Param("taskId") Long taskId);

    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.task_status = 'MISSED' WHERE t.id = :taskId")
    void markTaskAsMissed(@Param("taskId") Long taskId);

    @Query("SELECT t FROM Task t JOIN t.assignments ta WHERE ta.user.id = :user_id AND (t.task_status = 'MISSED' OR t.task_status = 'Missed')")
    List<Task> findMissedTasksByUserId(@Param("user_id") Long userId);

}
