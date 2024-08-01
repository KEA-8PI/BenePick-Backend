package com._pi.benepick.domain.pointHists.repository;

import com._pi.benepick.domain.pointHists.entity.PointHists;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


public interface PointHistsRepository extends JpaRepository<PointHists, Long> {

    @Query("select p from PointHists p WHERE p.memberId.id=:id")
    List<PointHists> findAllByMemberId(String id);


    @Modifying
    @Query("update PointHists p set p.content =:content, p.pointChange =:pointChange where p.memberId.id =:id")
    void updatePointHist(String id, Long pointChange,String content);
}
