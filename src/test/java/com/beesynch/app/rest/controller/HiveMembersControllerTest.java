package com.beesynch.app.rest.controller;

import com.beesynch.app.rest.Controller.HiveMembersController;
import com.beesynch.app.rest.DTO.HiveMembersDTO;
import com.beesynch.app.rest.DTO.MembersTaskListDTO;
import com.beesynch.app.rest.Models.HiveMembers;
import com.beesynch.app.rest.Repo.HiveMembersRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class HiveMembersControllerTest {

    @Mock
    private HiveMembersRepo hiveMembersRepo;

    @InjectMocks
    private HiveMembersController hiveMembersController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(hiveMembersController).build();
    }

    @Test
    public void testGetAllHiveMembers() throws Exception {
        // Mocking the repository to return a list of hive members
        List<HiveMembersDTO> hiveMembers = Arrays.asList(new HiveMembersDTO(), new HiveMembersDTO());
        when(hiveMembersRepo.getAllHiveMembers()).thenReturn(hiveMembers);

        mockMvc.perform(get("/HiveMembers/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(hiveMembersRepo, times(1)).getAllHiveMembers();
    }

    @Test
    public void testGetHiveMemberByUsername_Success() throws Exception {
        String username = "johnDoe";
        HiveMembersDTO hiveMember = new HiveMembersDTO();
        when(hiveMembersRepo.findByUsername(username)).thenReturn(hiveMember);

        mockMvc.perform(get("/HiveMembers/username/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(hiveMembersRepo, times(1)).findByUsername(username);
    }

    @Test
    public void testGetHiveMemberByUsername_NotFound() throws Exception {
        String username = "johnDoe";
        when(hiveMembersRepo.findByUsername(username)).thenReturn(null);

        mockMvc.perform(get("/HiveMembers/username/{username}", username))
                .andExpect(status().isNotFound());

        verify(hiveMembersRepo, times(1)).findByUsername(username);
    }

    @Test
    public void testGetMembersTaskListInfo() throws Exception {
        List<MembersTaskListDTO> taskList = Arrays.asList(new MembersTaskListDTO(), new MembersTaskListDTO());
        when(hiveMembersRepo.getMembersTaskListInfo()).thenReturn(taskList);

        mockMvc.perform(get("/HiveMembers/membersInfo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(hiveMembersRepo, times(1)).getMembersTaskListInfo();
    }


}
