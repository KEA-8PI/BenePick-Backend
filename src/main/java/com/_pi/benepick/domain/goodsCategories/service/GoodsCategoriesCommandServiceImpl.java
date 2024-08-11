package com._pi.benepick.domain.goodsCategories.service;

import com._pi.benepick.domain.categories.entity.Categories;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goodsCategories.entity.GoodsCategories;
import com._pi.benepick.domain.goodsCategories.repository.GoodsCategoriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class GoodsCategoriesCommandServiceImpl implements GoodsCategoriesCommandService {
    private final GoodsCategoriesRepository goodsCategoriesRepository;
    @Override
    public GoodsCategories createGoodsCategories(Goods goods, Categories categories) {
        GoodsCategories goodsCategories = GoodsCategories.builder()
            .goodsId(goods)
            .categoryId(categories)
            .build();
        goodsCategoriesRepository.save(goodsCategories);

        return goodsCategories;
    }
}
