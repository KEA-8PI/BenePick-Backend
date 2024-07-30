package com._pi.benepick.domain.penaltyHists.repository;

import com._pi.benepick.domain.penaltyHists.entity.PenaltyHists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PenaltyHistsRepository extends JpaRepository<PenaltyHists, Long> {
    @Query("select p from PenaltyHists  p where p.memberId.id =:id")
    List<PenaltyHists> findAllByMemberId(String id);
}
