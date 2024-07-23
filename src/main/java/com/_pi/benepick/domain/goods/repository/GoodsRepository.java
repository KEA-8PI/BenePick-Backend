package com._pi.benepick.domain.goods.repository;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {

    @Modifying
    @Query("UPDATE Goods g SET g.name = :name, g.amounts = :amounts, g.image = :image, g.description = :description, g.price = :price, g.discountPrice = :discountPrice, g.raffleStartAt = :raffleStartAt, g.raffleEndAt = :raffleEndAt, g.goodsStatus = :goodsStatus WHERE g.id = :goodsId")
    void updateGoods(Long goodsId, String name, Long amounts, String image, String description, Long price, Long discountPrice, LocalDateTime raffleStartAt, LocalDateTime raffleEndAt, GoodsStatus goodsStatus);
}
