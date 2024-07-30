package com._pi.benepick.domain.pointHists.repository;

import com._pi.benepick.domain.pointHists.entity.PointHists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PointHistsRepository extends JpaRepository<PointHists, Long> {

    @Query("select p from PointHists p where p.memberId.id =:id")
    PointHists findAllByMemberId(String id);


    @Modifying
    @Query("update PointHists p set p.content =:content, p.pointChange =:pointChange where p.memberId.id =:id")
    void updatePointHist(String id, Long pointChange,String content);
}
