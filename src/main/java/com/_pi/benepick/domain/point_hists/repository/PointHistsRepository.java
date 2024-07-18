package com._pi.benepick.domain.point_hists.repository;

import com._pi.benepick.domain.point_hists.entity.PointHists;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistsRepository extends JpaRepository<PointHists, Long> {
}
