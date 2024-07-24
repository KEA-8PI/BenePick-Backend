package com._pi.benepick.domain.winners.repository;

import com._pi.benepick.domain.winners.entity.Winners;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WinnersRepository extends JpaRepository<Winners, Long> {
}
