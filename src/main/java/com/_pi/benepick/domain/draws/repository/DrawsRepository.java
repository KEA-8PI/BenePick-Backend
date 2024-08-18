package com._pi.benepick.domain.draws.repository;

import com._pi.benepick.domain.draws.dto.DrawsResponse.DrawsAndGoodsCategory;
import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.draws.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrawsRepository extends JpaRepository<Draws, Long> {
    @Query("SELECT d FROM Draws d LEFT JOIN Raffles r ON d.raffleId.id = r.id WHERE r.goodsId.id = :goodsId ORDER BY " +
            "CASE d.status " +
            "WHEN 'WINNER' THEN 1 " +
            "WHEN 'WAITLIST' THEN 2 " +
            "WHEN 'CANCEL' THEN 3 " +
            "WHEN 'NO_SHOW' THEN 4 " +
            "WHEN 'NON_WINNER' THEN 5 " +
            "WHEN 'CONFIRM' THEN 6 " +
            "ELSE 7 END ASC, d.sequence ASC")
    List<Draws> findByGoodsId(@Param("goodsId") Long goodsId);

    @Query("SELECT new com._pi.benepick.domain.draws.dto.DrawsResponse$DrawsAndGoodsCategory(d, c.name) " +
            "FROM Draws d " +
            "LEFT JOIN Raffles r ON d.raffleId.id = r.id " +
            "LEFT JOIN Goods g ON r.goodsId.id = g.id " +
            "LEFT JOIN GoodsCategories gc ON g.id = gc.goodsId.id " +
            "LEFT JOIN Categories c ON gc.categoryId.id = c.id " +
            "WHERE r.memberId.id = :memberId ORDER BY d.id")
    List<DrawsAndGoodsCategory> findDrawsAndGoodsCategoryByMemberId(@Param("memberId") String memberId);

    @Query("SELECT d FROM Draws d LEFT JOIN Raffles r ON d.raffleId.id = r.id WHERE r.goodsId.id = :goodsId AND d.status = :status ORDER BY d.sequence ASC")
    List<Draws> findAllByGoodsIdAndStatus(@Param("goodsId") Long goodsId, @Param("status") Status status);

    @Query("SELECT d FROM Draws d WHERE d.raffleId.goodsId.id = :goodsId AND d.status IN :statuses")
    List<Draws> findDrawsByGoodsIdAndStatuses(Long goodsId, List<Status> statuses);

    @Query("SELECT COUNT(d) FROM Draws d WHERE d.raffleId.id IN :raffleIds AND d.status IN :statuses")
    long countByRaffleIdsAndStatuses(List<Long> raffleIds, List<Status> statuses);

    @Query("SELECT AVG(d.raffleId.point) FROM Draws d WHERE d.raffleId.goodsId.id = :goodsId AND d.status IN :statuses")
    Double findAveragePointByGoodsIdAndStatuses(Long goodsId, List<Status> statuses);
}
