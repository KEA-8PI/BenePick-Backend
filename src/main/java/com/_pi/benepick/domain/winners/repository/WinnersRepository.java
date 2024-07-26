package com._pi.benepick.domain.winners.repository;

import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.domain.winners.entity.Winners;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WinnersRepository extends JpaRepository<Winners, Long> {
    Optional<Winners> findByRaffleId(Raffles raffleId);

}
