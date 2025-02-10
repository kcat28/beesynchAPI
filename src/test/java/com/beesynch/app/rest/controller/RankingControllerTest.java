package com.beesynch.app.rest.controller;

import com.beesynch.app.rest.Controller.RankingController;
import com.beesynch.app.rest.DTO.RankingDTO;
import com.beesynch.app.rest.Models.Ranking;
import com.beesynch.app.rest.Models.User; // Assuming User is another model
import com.beesynch.app.rest.Models.Hive;  // Assuming Hive is another model
import com.beesynch.app.rest.Repo.RankingRepo;
import com.beesynch.app.rest.Service.RankingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class RankingControllerTest {

    @Mock
    private RankingRepo rankingRepo;

    @Mock
    private RankingService rankingService;

    @InjectMocks
    private RankingController rankingController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(rankingController).build();
    }

    @Test
    public void testGetWholeGroupRanking() throws Exception {
        // Mocking the repository to return a list of rankings with mocked user and hive
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);

        Hive hive = mock(Hive.class);
        when(hive.getHive_id()).thenReturn(1L);

        Ranking ranking1 = new Ranking(); // Set appropriate fields, including user and hive
        ranking1.setUser_id(user);
        ranking1.setHive_id(hive);
        ranking1.setRank_position(1);

        Ranking ranking2 = new Ranking(); // Similarly set fields for the second ranking
        ranking2.setUser_id(user);
        ranking2.setHive_id(hive);
        ranking2.setRank_position(2);

        when(rankingRepo.findAll()).thenReturn(Arrays.asList(ranking1, ranking2));

        mockMvc.perform(get("/ranking/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2)); // Check if we get 2 ranking records in response

        verify(rankingRepo, times(1)).findAll();
    }

    @Test
    public void testGetUserRanking_Success() throws Exception {
        Long userId = 1L;
        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        Hive hive = mock(Hive.class);
        when(hive.getHive_id()).thenReturn(1L);

        Ranking ranking = new Ranking();
        ranking.setUser_id(user);
        ranking.setHive_id(hive);
        ranking.setRank_position(1);

        when(rankingRepo.findById(userId)).thenReturn(Optional.of(ranking));

        mockMvc.perform(get("/ranking/getbyUserId/{user_id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(userId)); // Check if the correct user ranking is returned

        verify(rankingRepo, times(1)).findById(userId);
    }

    @Test
    public void testGetUserRanking_NotFound() throws Exception {
        Long userId = 1L;
        when(rankingRepo.findById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ranking/getbyUserId/{user_id}", userId))
                .andExpect(status().isNotFound());

        verify(rankingRepo, times(1)).findById(userId);
    }

    @Test
    public void testCreateNewRankingData() throws Exception {
        // Mocking the necessary fields
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L); // Mocking the ID of the user

        Hive hive = mock(Hive.class);
        when(hive.getHive_id()).thenReturn(1L); // Mocking the ID of the hive

        // Create the Ranking object and set the necessary values
        Ranking ranking = new Ranking();
        ranking.setUser_id(user);
        ranking.setHive_id(hive);
        ranking.setRank_position(1);
        ranking.setPeriod_start(Date.valueOf("2025-01-01"));
        ranking.setPeriod_end(Date.valueOf("2025-01-31"));

        // Mocking the repository save method
        when(rankingRepo.save(any(Ranking.class))).thenReturn(ranking);

        String json = "{"
                + "\"rank_position\": 1, "
                + "\"user_id\": {\"id\": 1}, "
                + "\"hive_id\": {\"hive_id\": 1}, "
                + "\"period_start\": \"2025-01-01\", "
                + "\"period_end\": \"2025-01-31\""
                + "}";

        mockMvc.perform(post("/ranking/createNewRankingData")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.rank_position").value(1)); // Check if rank position is 1

        // Verify that the repository save method was called once
        verify(rankingRepo, times(1)).save(any(Ranking.class));
    }


    @Test
    public void testDeleteRankData() throws Exception {
        Long rankId = 1L;

        mockMvc.perform(delete("/ranking/DeleteRankData/{rankId}", rankId))
                .andExpect(status().isOk())
                .andExpect(content().string("deleted ranking with id of: " + rankId));

        verify(rankingRepo, times(1)).deleteById(rankId);
    }

    @Test
    public void testUpdateLeaderboard() throws Exception {
        // This test is for the manual update endpoint
        doNothing().when(rankingService).updateLeaderboardWeekly();

        mockMvc.perform(get("/ranking/updateLeaderboard"))
                .andExpect(status().isOk())
                .andExpect(content().string("for manual update (testing only)"));

        verify(rankingService, times(1)).updateLeaderboardWeekly();
    }
}
