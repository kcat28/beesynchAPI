package com.beesynch.app.rest.Controller;

import com.beesynch.app.rest.DTO.RankingDTO;
import com.beesynch.app.rest.Models.Ranking;
import com.beesynch.app.rest.Repo.RankingRepo;
import com.beesynch.app.rest.Service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/ranking")
    public class RankingController {

    @Autowired
    private RankingRepo rankingRepo;

    @Autowired
    private RankingService rankingService;

    // controller is for manual update only/ automated every 00:00 monday ph time
    @GetMapping("/updateLeaderboard")
    public String updateLeaderboard() {
        rankingService.updateLeaderboardWeekly();
        return "for manual update (testing only)";
    }

    @GetMapping("/")
    public List<RankingDTO> getWholeGroupRanking(){
        return rankingRepo.findAll().stream()
                .map(ranking -> new RankingDTO(
                        ranking.getRanking_id(),
                        ranking.getUser_id().getId(),
                        ranking.getHive_id().getHive_id(),
                        ranking.getRank_position(),
                        ranking.getPeriod_start(),
                        ranking.getPeriod_end()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/getbyUserId/{user_id}")
    public ResponseEntity<RankingDTO> getUserRanking(@PathVariable Long user_id) {
        Optional<Ranking> rankingOptional = rankingRepo.findById(user_id);
        if (rankingOptional.isPresent()) {
            Ranking ranking = rankingOptional.get();
            RankingDTO rankingDTO = new RankingDTO(
                    ranking.getRanking_id(),
                    ranking.getUser_id().getId(),
                    ranking.getHive_id().getHive_id(),
                    ranking.getRank_position(),
                    ranking.getPeriod_start(),
                    ranking.getPeriod_end()
            );
            return ResponseEntity.ok(rankingDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/createNewRankingData")
    public Ranking createRankData(@RequestBody Ranking RankData){
        return rankingRepo.save(RankData);
    }

    @DeleteMapping("/DeleteRankData/{rankId}")// change to response entity
    public String deleteById(@PathVariable Long rankId){
        rankingRepo.deleteById(rankId);
        return "deleted ranking with id of: " + rankId;
    }

}
