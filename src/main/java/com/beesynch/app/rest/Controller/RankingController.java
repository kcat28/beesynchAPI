package com.beesynch.app.rest.Controller;

import com.beesynch.app.rest.DTO.RankingDTO;
import com.beesynch.app.rest.Models.Ranking;
import com.beesynch.app.rest.Repo.RankingRepo;
import org.springframework.beans.factory.annotation.Autowired;
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
    public RankingDTO getUserRanking(@PathVariable Long user_id){
        // find ranking by user_id
        Optional<Ranking> rankingOptional = rankingRepo.findById(user_id);

        if (rankingOptional.isPresent()) {
            Ranking ranking = rankingOptional.get();

            return new RankingDTO(
                    ranking.getRanking_id(),
                    ranking.getUser_id().getId(),
                    ranking.getHive_id().getHive_id(),
                    ranking.getRank_position(),
                    ranking.getPeriod_start(),
                    ranking.getPeriod_end()
            );
        } else {
            return null;
        }
    }



    @PostMapping("/createNewRankingData")
    public Ranking createRankData(@RequestBody Ranking RankData){
        return rankingRepo.save(RankData);
    }

    @DeleteMapping("/DeleteRankData/{rankId}")
    public String deleteById(@PathVariable Long rankId){
        rankingRepo.deleteById(rankId);
        return "deleted ranking with id of: " + rankId;
    }

}
