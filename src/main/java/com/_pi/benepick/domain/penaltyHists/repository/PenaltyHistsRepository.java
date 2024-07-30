package com._pi.benepick.domain.penaltyHists.repository;

import com._pi.benepick.domain.penaltyHists.entity.PenaltyHists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PenaltyHistsRepository extends JpaRepository<PenaltyHists, Long> {
    @Modifying
    @Query("update PenaltyHists p set p.content =:content, p.penaltyCount =:cnt where p.memberId.id =:id")
    void updatePenaltyHist(String id,int cnt, String content);
}
