package com._pi.benepick.domain.pointHists.repository;

import com._pi.benepick.domain.pointHists.entity.PointHists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PointHistsRepository extends JpaRepository<PointHists, Long> {

    @Query("select p from PointHists p WHERE p.memberId.id=:id group by p order by p.createdAt desc ")
    Page<PointHists> findAllByMemberId(Pageable pageable, String id);

    void deleteAllByMemberId_Id(String id);
    int countAllByMemberId_Id(String id);
}
