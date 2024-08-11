package com._pi.benepick.domain.wishlists.repository;

import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.wishlists.entity.Wishlists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

public interface WishlistsRepository extends JpaRepository<Wishlists, Long> {
    @Query("SELECT w FROM Wishlists w " +
            "LEFT JOIN Raffles r ON w.goodsId.id = r.goodsId.id " +
            "WHERE w.memberId.id = :memberId AND w.goodsId.goodsStatus = :goodsStatus " +
            "GROUP BY w.id, w.goodsId.id " +
            "ORDER BY COUNT(r.id) DESC, w.goodsId.id ASC")
    Page<Wishlists> searchWishlistsByRaffleCount(String memberId,
                                                  GoodsStatus goodsStatus,
                                                 Pageable pageable);
    Page<Wishlists> findAllByMemberId_IdAndGoodsId_GoodsStatus(String memberId, GoodsStatus goodsStatus, Pageable pageable);

void deleteAllByMemberId_Id(String id);

}
