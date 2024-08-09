package com._pi.benepick.domain.pointHists.repository;

import com._pi.benepick.domain.pointHists.entity.PointHists;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface PointHistsRepository extends JpaRepository<PointHists, Long> {

    @Query("select p from PointHists p WHERE p.memberId.id=:id")
    Page<PointHists> findAllByMemberId(Pageable pageable, String id);

    void deleteAllByMemberId_Id(String id);
    int countAllByMemberId_Id(String id);

    void deleteAllByMemberId_Id(String id);

}
