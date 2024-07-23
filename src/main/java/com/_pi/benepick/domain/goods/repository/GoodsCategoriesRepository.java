package com._pi.benepick.domain.goods.repository;

import com._pi.benepick.domain.goods.entity.GoodsCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GoodsCategoriesRepository extends JpaRepository<GoodsCategories, Long> {
    Optional<GoodsCategories> findByGoodsId(Long goodsId);
}
