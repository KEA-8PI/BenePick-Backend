package com._pi.benepick.domain.goodsCategories.service;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goodsCategories.entity.GoodsCategories;
import com._pi.benepick.domain.goodsCategories.repository.GoodsCategoriesRepository;
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoodsCategoriesQueryServiceImpl implements GoodsCategoriesQueryService {
    private final GoodsCategoriesRepository goodsCategoriesRepository;

    @Override
    public GoodsCategories getGoodsCategoriesByGoodsId(Goods goods){
        return goodsCategoriesRepository.findByGoodsId(goods).orElseThrow(()->new ApiException(ErrorStatus._GOODS_NOT_FOUND));
    }
}
