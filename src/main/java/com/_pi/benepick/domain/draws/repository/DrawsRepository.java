package com._pi.benepick.domain.draws.repository;

import com._pi.benepick.domain.draws.entity.Draws;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrawsRepository extends JpaRepository<Draws, Long> {
//    Optional<Draws> findByRaffleId(Raffles raffleId);

    // Custom query to find winners by goodsId
    @Query("SELECT d FROM Draws d LEFT JOIN Raffles r ON d.raffleId.id = r.id WHERE r.goodsId.id = :goodsId")
    List<Draws> findByGoodsId(@Param("goodsId") Long goodsId);

    @Query("SELECT d FROM Draws d LEFT JOIN Raffles r ON d.raffleId.id = r.id WHERE r.memberId.id = :memberId")
    List<Draws> findByMemberId(@Param("memberId") String memberId);

}
