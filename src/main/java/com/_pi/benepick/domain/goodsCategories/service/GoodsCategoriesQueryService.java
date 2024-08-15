package com._pi.benepick.domain.goodsCategories.service;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goodsCategories.entity.GoodsCategories;

public interface GoodsCategoriesQueryService {
    GoodsCategories getGoodsCategoriesByGoodsId(Goods goods);
}
