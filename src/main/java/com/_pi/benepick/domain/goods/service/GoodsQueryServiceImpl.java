package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.categories.entity.Categories;
import com._pi.benepick.domain.goodsCategories.entity.GoodsCategories;
import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.categories.repository.CategoriesRepository;
import com._pi.benepick.domain.goodsCategories.repository.GoodsCategoriesRepository;
import com._pi.benepick.domain.goods.repository.GoodsRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsQueryServiceImpl implements GoodsQueryService{

    private final GoodsRepository goodsRepository;
    private final GoodsCategoriesRepository goodsCategoriesRepository;
    private final CategoriesRepository categoriesRepository;

    // 상품 상세 조회
    @Override
    public GoodsResponse.GoodsResponseDTO getGoodsInfo(Long goodsId) {
        Goods goods = goodsRepository.findById(goodsId).orElseThrow(() -> new ApiException(ErrorStatus._GOODS_NOT_FOUND));
        GoodsCategories goodsCategories = goodsCategoriesRepository.findByGoodsId(goods).orElseThrow(() -> new ApiException(ErrorStatus._GOODS_CATEGORY_NOT_FOUND));
        Categories category = categoriesRepository.findById(goodsCategories.getCategoryId().getId()).orElseThrow(()-> new ApiException(ErrorStatus._CATEGORY_NOT_FOUND));
        return GoodsResponse.GoodsResponseDTO.builder()
                .id(goods.getId())
                .name(goods.getName())
                .amounts(goods.getAmounts())
                .image(goods.getImage())
                .description(goods.getDescription())
                .goodsStatus(String.valueOf(goods.getGoodsStatus()))
                .price(goods.getPrice())
                .discountPrice(goods.getDiscountPrice())
                .raffleStartAt(goods.getRaffleStartAt())
                .raffleEndAt(goods.getRaffleEndAt())
                .category(category.getName())
                .build();
    }

    // 상품 목록 조회
    public List<GoodsResponse.GoodsResponseDTO> getGoodsList() {
        List<Goods> goodsList = goodsRepository.findAll();

        Map<Long, Long> goodsCategoryMap = goodsCategoriesRepository.findAll().stream()
                .collect(Collectors.toMap(
                    goodsCategories -> goodsCategories.getGoodsId().getId(),
                    goodsCategories -> goodsCategories.getCategoryId().getId()
                ));

        Map<Long, String> categoriesMap = categoriesRepository.findAll().stream()
                .collect(Collectors.toMap(Categories::getId, Categories::getName));

        return goodsList.stream()
                .map(goods -> GoodsResponse.GoodsResponseDTO.builder()
                        .id(goods.getId())
                        .name(goods.getName())
                        .amounts(goods.getAmounts())
                        .image(goods.getImage())
                        .description(goods.getDescription())
                        .goodsStatus(String.valueOf(goods.getGoodsStatus()))
                        .price(goods.getPrice())
                        .discountPrice(goods.getDiscountPrice())
                        .raffleStartAt(goods.getRaffleStartAt())
                        .raffleEndAt(goods.getRaffleEndAt())
                        .category(categoriesMap.get(goodsCategoryMap.get(goods.getId())))
                        .build())
                .collect(Collectors.toList());
    }

    //시드 값 조회
    public String getSeeds(Long goodsId) {
        Goods goods = goodsRepository.findById(goodsId).orElseThrow(() -> new ApiException(ErrorStatus._GOODS_NOT_FOUND));
        return goods.getSeeds();
    }
}
