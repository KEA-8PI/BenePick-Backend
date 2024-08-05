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
<<<<<<< HEAD
    @Modifying

    @Query("delete from PointHists p where p.memberId.id =:id")
    void deleteAllByMemberId(String id);
=======

>>>>>>> 4351767d7e5f60ee112bf68d01663d17fae51d2f
}
