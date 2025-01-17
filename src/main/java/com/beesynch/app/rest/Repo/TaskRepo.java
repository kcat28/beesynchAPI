package com.beesynch.app.rest.Repo;

import com.beesynch.app.rest.DTO.TaskCreationRequestDTO;
import com.beesynch.app.rest.Models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task, Long>{

    @Query("SELECT t FROM Task t LEFT JOIN TaskAssignment ta ON t.id = ta.task.id WHERE ta.user IS NULL")
    List<Task> findUnassignedTasks();

//    @Query(
//    "SELECT new com.beesynch.app.rest.DTO.TaskCreationRequestDTO(t.title, s.dueTime, s.endDate) " +
//    "FROM Task t " +
//    "JOIN Schedule s ON t.id = s.task.id"
//)
//    List<TaskCreationRequestDTO> findTaskSchedules();

    //01/19/2025
    @Query("SELECT t.title, s.end_date, s.due_time FROM Task t JOIN t.schedule s")
    List<Object[]> findTaskDetails();

}
