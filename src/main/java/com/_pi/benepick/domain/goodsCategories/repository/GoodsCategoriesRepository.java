package com._pi.benepick.domain.goodsCategories.repository;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goodsCategories.entity.GoodsCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GoodsCategoriesRepository extends JpaRepository<GoodsCategories, Long> {
    Optional<GoodsCategories> findByGoodsId(Goods goodsId);
}
