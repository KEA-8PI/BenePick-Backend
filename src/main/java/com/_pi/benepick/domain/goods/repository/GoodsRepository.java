package com._pi.benepick.domain.goods.repository;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {
    @Query("SELECT g FROM Goods g WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Goods> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Modifying
    @Query("UPDATE Goods g SET g.name = :name, g.amounts = :amounts, g.image = :image, g.description = :description, g.price = :price, g.discountPrice = :discountPrice, g.raffleStartAt = :raffleStartAt, g.raffleEndAt = :raffleEndAt, g.goodsStatus = :goodsStatus WHERE g.id = :goodsId")
    void updateGoods(Long goodsId, String name, Long amounts, String image, String description, Long price, Long discountPrice, LocalDateTime raffleStartAt, LocalDateTime raffleEndAt, GoodsStatus goodsStatus);

    @Query("SELECT g FROM Goods g " +
            "WHERE g.goodsStatus = :goodsStatus " +
            "AND (:categoryId IS NULL OR EXISTS (SELECT 1 FROM GoodsCategories gc WHERE gc.goodsId.id = g.id AND gc.categoryId.id = :categoryId)) " +
            "AND (:keyword IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Goods> searchGoods(GoodsStatus goodsStatus, Long categoryId, String keyword, Pageable pageable);

    @Query("SELECT g FROM Goods g LEFT JOIN g.raffles r " +
            "WHERE (:keyword IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :keyword, '%')))" +
            "AND g.goodsStatus = :goodsStatus " +
            "AND (:categoryId IS NULL OR EXISTS (SELECT 1 FROM GoodsCategories gc WHERE gc.goodsId.id = g.id AND gc.categoryId.id = :categoryId)) " +
            "GROUP BY g " +
            "ORDER BY COUNT(r) DESC, g.id ASC")
    Page<Goods> searchGoodsByRaffleCount(GoodsStatus goodsStatus, Long categoryId, String keyword, Pageable pageable);
}
