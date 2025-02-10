package com.beesynch.app.rest.service;
import com.beesynch.app.rest.Models.Hive;
import com.beesynch.app.rest.Models.Ranking;
import com.beesynch.app.rest.Models.User;
import com.beesynch.app.rest.Repo.HiveRepo;
import com.beesynch.app.rest.Repo.RankingRepo;
import com.beesynch.app.rest.Repo.UserRepo;
import com.beesynch.app.rest.Service.RankingService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RankingServiceTest {

    @InjectMocks
    private RankingService rankingService;

    @Mock
    private RankingRepo rankingRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private HiveRepo hiveRepo;

    @Mock
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testUpdateLeaderBoard() {
        // Mock data for leaderboard
        List<Object[]> leaderboardRaw = Arrays.asList(
                new Object[]{1L, 100L, 1L},
                new Object[]{2L, 90L, 1L},
                new Object[]{3L, 90L, 2L}
        );

        when(rankingRepo.getLeaderboard()).thenReturn(leaderboardRaw);
        when(userRepo.findById(1L)).thenReturn(Optional.of(new User()));
        when(userRepo.findById(2L)).thenReturn(Optional.of(new User()));
        when(userRepo.findById(3L)).thenReturn(Optional.of(new User()));
        when(hiveRepo.findById(1L)).thenReturn(Optional.of(new Hive()));
        when(hiveRepo.findById(2L)).thenReturn(Optional.of(new Hive()));

        rankingService.updateLeaderBoard();

        verify(rankingRepo, times(3)).save(any(Ranking.class));
    }

    @Test
    void testDeleteLeaderBoard() {
        rankingService.deleteLeaderBoard();
        verify(rankingRepo, times(1)).deleteAll();
        verify(entityManager, times(1)).flush();
        verify(entityManager, times(1)).clear();
    }
}
