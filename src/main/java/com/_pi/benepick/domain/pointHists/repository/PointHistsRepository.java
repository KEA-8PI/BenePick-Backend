package com._pi.benepick.domain.pointHists.repository;

import com._pi.benepick.domain.pointHists.entity.PointHists;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistsRepository extends JpaRepository<PointHists, Long> {
}
