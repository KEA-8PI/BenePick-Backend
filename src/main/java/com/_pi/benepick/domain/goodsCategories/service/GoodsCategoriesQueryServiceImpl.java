package com._pi.benepick.domain.goodsCategories.service;

import com._pi.benepick.domain.goodsCategories.repository.GoodsCategoriesRepository;
import com._pi.benepick.domain.raffles.entity.Raffles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsCategoriesQueryServiceImpl implements GoodsCategoriesQueryService {

    private final GoodsCategoriesRepository goodsCategoriesRepository;

    public String getGoodsCategory(Raffles raffles) {
        String categoryName = (goodsCategoriesRepository.findByGoodsId(raffles.getGoodsId()))
                .map(goodsCategories -> goodsCategories.getCategoryId().getName()).orElse("NONE");
        return categoryName;
    }
}
