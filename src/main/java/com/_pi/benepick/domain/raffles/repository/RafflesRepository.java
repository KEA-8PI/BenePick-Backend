package com._pi.benepick.domain.raffles.repository;

import com._pi.benepick.domain.raffles.dto.RafflesResponse;
import com._pi.benepick.domain.raffles.entity.Raffles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RafflesRepository extends JpaRepository<Raffles, Long> {

    @Query("SELECT new com._pi.benepick.domain.raffles.dto.RafflesResponse$RafflesResponseByGoodsDTO(" +
            "    r.id, " +
            "    r.memberId.id, " +
            "    r.memberId.name, " +
            "    r.goodsId.id, " +
            "    r.point, " +
            "    w.sequence, " +
            "    COALESCE(w.status, 'SCHEDULED'), " +
            "    r.updated_at " +
            ") " +
            "FROM Raffles r " +
            "LEFT JOIN Winners w ON r.id = w.raffleId.id " +
            "WHERE r.goodsId.id = :goodsId")
    List<RafflesResponse.RafflesResponseByGoodsDTO> findAllByGoodsId(@Param("goodsId") Long goodsId);

    @Query("SELECT new com._pi.benepick.domain.raffles.dto.RafflesResponse$RafflesResponseByMembersDTO(" +
            "    r.id, " +
            "    r.memberId.id, " +
            "    r.goodsId.id, " +
            "    r.point, " +
            "    w.sequence, " +
            "    COALESCE(w.status, 'SCHEDULED'), " +
            "    r.updated_at, " +
            "    c.name " +  // Add category name
            ") " +
            "FROM Raffles r " +
            "LEFT JOIN Winners w ON r.id = w.raffleId.id " +
            "LEFT JOIN Goods g ON r.goodsId.id = g.id " +  // Join Goods
            "LEFT JOIN Categories gc ON g.id = gc.id " +  // Join GoodsCategories
            "LEFT JOIN Categories c ON gc.id = c.id " +  // Join Categories
            "WHERE r.memberId.id = :memberId")
    List<RafflesResponse.RafflesResponseByMembersDTO> findAllByMemberId(@Param("memberId") String memberId);
}