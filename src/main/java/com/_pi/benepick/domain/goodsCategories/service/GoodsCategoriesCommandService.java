package com._pi.benepick.domain.goodsCategories.service;

import com._pi.benepick.domain.categories.entity.Categories;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goodsCategories.entity.GoodsCategories;

public interface GoodsCategoriesCommandService {

    GoodsCategories createGoodsCategories(Goods goods, Categories categories);

}
