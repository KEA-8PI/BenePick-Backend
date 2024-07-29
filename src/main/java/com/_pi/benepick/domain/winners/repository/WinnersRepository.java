package com._pi.benepick.domain.winners.repository;

import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.domain.winners.entity.Winners;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WinnersRepository extends JpaRepository<Winners, Long> {
    Optional<Winners> findByRaffleId(Raffles raffleId);

    // Custom query to find winners by goodsId
    @Query("SELECT w FROM Winners w LEFT JOIN Raffles r ON w.raffleId.id = r.id WHERE r.goodsId.id = :goodsId")
    List<Winners> findByGoodsId(@Param("goodsId") Long goodsId);

    @Query("SELECT w FROM Winners w LEFT JOIN Raffles r ON w.raffleId.id = r.id WHERE r.memberId.id = :memberId")
    List<Winners> findByMemberId(@Param("memberId") String memberId);

}
