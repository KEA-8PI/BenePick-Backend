package com._pi.benepick.domain.hash.repository;

import com._pi.benepick.domain.hash.entity.Hash;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashsRepository extends JpaRepository<Hash, Long> {
    Optional<Hash> findByHash(String hash);
}
