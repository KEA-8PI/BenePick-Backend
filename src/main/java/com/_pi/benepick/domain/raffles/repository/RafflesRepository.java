package com._pi.benepick.domain.raffles.repository;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.raffles.dto.RafflesResponse.RafflesAndGoodsCategory;
import com._pi.benepick.domain.raffles.entity.Raffles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RafflesRepository extends JpaRepository<Raffles, Long> {

    List<Raffles> findAllByGoodsIdOrderByPointAsc(Goods goods);

    List<Raffles> findAllByGoodsIdOrderByPointDesc(Goods goods);

    List<Raffles> findAllByGoodsId(Goods goods);

    @Query("SELECT new com._pi.benepick.domain.raffles.dto.RafflesResponse$RafflesAndGoodsCategory(r, c.name) " +
            "FROM Raffles r " +
            "LEFT JOIN Goods g ON r.goodsId.id = g.id " +
            "LEFT JOIN GoodsCategories gc ON g.id = gc.goodsId.id " +
            "LEFT JOIN Categories c ON gc.categoryId.id = c.id " +
            "WHERE r.memberId.id = :memberId ORDER BY r.id")
    List<RafflesAndGoodsCategory> findRafflsAndGoodsCategoryByMemberId(@Param("memberId") String memberId);

    Optional<Raffles> findByGoodsIdAndMemberId(Goods goods, Members members);

    void deleteAllByMemberId_IdAndGoodsId_GoodsStatus(String id,GoodsStatus goodsStatus);

}