package com._pi.benepick.domain.raffles.repository;

import com._pi.benepick.domain.raffles.entity.Winners;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WinnersRepository extends JpaRepository<Winners, Long> {
}
