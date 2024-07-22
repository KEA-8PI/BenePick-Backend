package com._pi.benepick.domain.goods.repository;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsCategories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsCategoriesRepository extends JpaRepository<GoodsCategories, Long> {
}
