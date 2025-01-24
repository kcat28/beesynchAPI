package com.beesynch.app.rest.Repo;

import com.beesynch.app.rest.DTO.HiveMembersDTO;
import com.beesynch.app.rest.DTO.MembersTaskListDTO;
import com.beesynch.app.rest.Models.HiveMemberId;
import com.beesynch.app.rest.Models.HiveMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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





}
