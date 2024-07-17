package com._pi.benepick.domain.raffles.repository;

import com._pi.benepick.domain.raffles.entity.Raffles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RafflesRepository extends JpaRepository<Raffles, Long> {
}
