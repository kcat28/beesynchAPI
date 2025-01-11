package com.beesynch.app.rest.Repo;

import com.beesynch.app.rest.Models.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RankingRepo extends JpaRepository<Ranking, Long>{
}
