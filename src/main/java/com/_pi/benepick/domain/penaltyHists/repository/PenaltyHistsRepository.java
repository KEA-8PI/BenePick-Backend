package com._pi.benepick.domain.penaltyHists.repository;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.penaltyHists.entity.PenaltyHists;
import com._pi.benepick.domain.pointHists.entity.PointHists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface PenaltyHistsRepository extends JpaRepository<PenaltyHists, Long> {

    @Query("select p from PenaltyHists p where p.memberId.id =:id")
    PenaltyHists findAllByMemberId(String id);

    Page<PenaltyHists> findAllByMemberId_Id(Pageable pageable, String id);
    void deleteAllByMemberId_Id(String id);

    int countAllByMemberId_Id(String id);

}
