package com._pi.benepick.domain.penalty_hists.repository;

import com._pi.benepick.domain.penalty_hists.entity.PenaltyHists;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PenaltyHistsRepository extends JpaRepository<PenaltyHists, Long> {
}
