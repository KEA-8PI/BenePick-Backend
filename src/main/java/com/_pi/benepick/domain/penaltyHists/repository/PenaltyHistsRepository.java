package com._pi.benepick.domain.penaltyHists.repository;

import com._pi.benepick.domain.penaltyHists.entity.PenaltyHists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PenaltyHistsRepository extends JpaRepository<PenaltyHists, Long> {

    Page<PenaltyHists> findAllByMemberId_IdOrderByCreatedAtDesc(Pageable pageable, String id);
    void deleteAllByMemberId_Id(String id);

    int countAllByMemberId_Id(String id);

}
