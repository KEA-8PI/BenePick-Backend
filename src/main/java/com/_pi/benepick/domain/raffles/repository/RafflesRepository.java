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
            "    r.member.id, " +
            "    r.member.name, " +
            "    r.goods.id, " +
            "    r.point, " +
            "    w.sequence, " +
            "    COALESCE(w.status, 'SCHEDULED'), " +
            "    r.updated_at " +
            ") " +
            "FROM Raffles r " +
            "LEFT JOIN Winners w ON r.id = w.raffle.id " +
            "WHERE r.goods.id = :goodsId")
    List<RafflesResponse.RafflesResponseByGoodsDTO> findAllByGoodsId(@Param("goodsId") Long goodsId);

    @Query("SELECT new com._pi.benepick.domain.raffles.dto.RafflesResponse$RafflesResponseByMembersDTO(" +
            "    r.id, " +
            "    r.member.id, " +
            "    r.goods.id, " +
            "    r.point, " +
            "    w.sequence, " +
            "    COALESCE(w.status, 'SCHEDULED'), " +
            "    r.updated_at, " +
            "    c.name " +  // Add category name
            ") " +
            "FROM Raffles r " +
            "LEFT JOIN Winners w ON r.id = w.raffle.id " +
            "LEFT JOIN Goods g ON r.goods.id = g.id " +  // Join Goods
            "LEFT JOIN GoodsCategories gc ON g.id = gc.goodsId " +  // Join GoodsCategories
            "LEFT JOIN Categories c ON gc.categoryId = c.id " +  // Join Categories
            "WHERE r.member.id = :memberId")
    List<RafflesResponse.RafflesResponseByMembersDTO> findAllByMemberId(@Param("memberId") String memberId);
}