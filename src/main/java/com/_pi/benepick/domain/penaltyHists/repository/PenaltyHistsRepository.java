package com._pi.benepick.domain.penaltyHists.repository;

import com._pi.benepick.domain.penaltyHists.entity.PenaltyHists;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PenaltyHistsRepository extends JpaRepository<PenaltyHists, Long> {
}
