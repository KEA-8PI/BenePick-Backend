package com._pi.benepick.domain.pointHists.repository;

import com._pi.benepick.domain.pointHists.entity.PointHists;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface PointHistsRepository extends JpaRepository<PointHists, Long> {

    @Query("select p from PointHists p WHERE p.memberId.id=:id")
    List<PointHists> findAllByMemberId(String id);

    void deleteAllByMemberId_Id(String id);

}
