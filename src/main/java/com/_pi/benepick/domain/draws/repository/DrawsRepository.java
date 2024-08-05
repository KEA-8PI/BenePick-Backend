package com._pi.benepick.domain.draws.repository;

import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.draws.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrawsRepository extends JpaRepository<Draws, Long> {
    @Query("SELECT d FROM Draws d LEFT JOIN Raffles r ON d.raffleId.id = r.id WHERE r.goodsId.id = :goodsId")
    List<Draws> findByGoodsId(@Param("goodsId") Long goodsId);

    @Query("SELECT d FROM Draws d LEFT JOIN Raffles r ON d.raffleId.id = r.id WHERE r.memberId.id = :memberId")
    List<Draws> findByMemberId(@Param("memberId") String memberId);

    @Query("SELECT d FROM Draws d WHERE d.raffleId.goodsId.id = :goodsId AND d.status IN :statuses")
    List<Draws> findDrawsByGoodsIdAndStatuses(Long goodsId, List<Status> statuses);

    @Query("SELECT d FROM Draws d WHERE d.raffleId.id = :raffleId AND d.status IN :statuses ORDER BY d.raffleId.point DESC")
    List<Draws> findDrawsByRaffleIdAndStatuses(Long raffleId, List<Status> statuses);

    @Query("SELECT COUNT(d) FROM Draws d WHERE d.raffleId.id IN :raffleIds AND d.status IN :statuses")
    long countByRaffleIdsAndStatuses(List<Long> raffleIds, List<Status> statuses);

    @Query("SELECT AVG(d.raffleId.point) FROM Draws d WHERE d.raffleId.goodsId.id = :goodsId AND d.status = :status")
    Double findAveragePointByGoodsIdAndStatus(Long goodsId, Status status);
}
