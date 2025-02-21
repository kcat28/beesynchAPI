package com.beesynch.app.rest.Repo;

import com.beesynch.app.rest.DTO.HiveMembersDTO;
import com.beesynch.app.rest.DTO.MembersTaskListDTO;
import com.beesynch.app.rest.Models.Task;
import com.beesynch.app.rest.Models.HiveMemberId;
import com.beesynch.app.rest.Models.HiveMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HiveMembersRepo extends JpaRepository<HiveMembers, Long>{

    @Query("SELECT new com.beesynch.app.rest.DTO.HiveMembersDTO(u.id, u.img_path, u.user_name, u.first_name, u.last_name, u.user_email, h.hive_id, h.hiveName, hm.role, hm.points, hm.achievements) " +
            "FROM HiveMembers hm " +
            "JOIN User u ON hm.id.userId = u.id " +  //  reference to userId in the composite key
            "JOIN Hive h ON hm.id.hiveId = h.hive_id " +  //  reference to hiveId in the composite key
            "WHERE u.user_name = :user_name")
    HiveMembersDTO findByUsername(@Param("user_name") String username);


    @Query("SELECT new com.beesynch.app.rest.DTO.HiveMembersDTO(u.id, u.img_path, u.user_name, u.first_name, u.last_name, u.user_email, h.hive_id, h.hiveName, hm.role, hm.points, hm.achievements) " +
            "FROM HiveMembers hm " +
            "JOIN User u ON hm.id.userId = u.id " +  // reference to userId in the composite key
            "JOIN Hive h ON hm.id.hiveId = h.hive_id") // reference to hiveId in the composite key
    List<HiveMembersDTO> getAllHiveMembers();

    @Query("SELECT new com.beesynch.app.rest.DTO.MembersTaskListDTO(u.id, u.user_name, u.img_path)" +
            "FROM HiveMembers hm " +
            "JOIN User u ON hm.id.userId = u.id ") // reference to userId in the composite key
    List<MembersTaskListDTO> getMembersTaskListInfo();

    @Modifying
    @Transactional
    @Query("DELETE FROM HiveMembers hm WHERE hm.user.id = :userId")
    void deleteHiveMember(@Param("userId") Long userId);

    @Query("SELECT COUNT(hm) > 0 FROM HiveMembers hm WHERE hm.user.id = :userId")
    boolean existsByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(t) FROM Task t " +
            "JOIN Schedule s ON t.id = s.task.id " +
            "JOIN HiveMembers h ON s.user_id.id = h.user.id " +
            "WHERE h.user.id = :userId AND t.task_status = 'Completed'")
    Long countCompletedTasksForUser(@Param("userId") Long userId);

    @Query("SELECT COUNT(t) FROM Task t " +
            "JOIN Schedule s ON t.id = s.task.id " +
            "JOIN HiveMembers h ON s.user_id.id = h.user.id " +
            "WHERE h.user.id = :userId")
    Long totalCountTasksForUser(@Param("userId") Long userId);

    @Query("SELECT hm.completionRate FROM HiveMembers hm WHERE hm.user.id = :userId")
    Double getCompletionRate(@Param("userId") Long userId);






}
