package com._pi.benepick.domain.pointHists.repository;

import com._pi.benepick.domain.pointHists.entity.PointHists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PointHistsRepository extends JpaRepository<PointHists, Long> {

    @Query("select PointHists from PointHists p WHERE p.memberId=:id")
    List<PointHists> findAllByMemberId(String id);
}
