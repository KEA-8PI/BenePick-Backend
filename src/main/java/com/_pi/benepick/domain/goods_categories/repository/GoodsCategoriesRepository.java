package com._pi.benepick.domain.goods_categories.repository;

import com._pi.benepick.domain.goods_categories.entity.GoodsCategories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoodsCategoriesRepository extends JpaRepository<GoodsCategories, Long> {
    Optional<GoodsCategories> findByGoodsId(Long goodsId);
}
