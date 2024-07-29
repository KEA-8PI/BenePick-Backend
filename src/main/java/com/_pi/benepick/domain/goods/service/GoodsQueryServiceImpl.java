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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsQueryServiceImpl implements GoodsQueryService {

    private final GoodsRepository goodsRepository;
    private final GoodsCategoriesRepository goodsCategoriesRepository;
    private final CategoriesRepository categoriesRepository;

    // 상품 상세 조회
    @Override
    public GoodsResponse.GoodsDetailResponseDTO getGoodsInfo(Long goodsId) {
        Goods goods = goodsRepository.findById(goodsId).orElseThrow(() -> new ApiException(ErrorStatus._GOODS_NOT_FOUND));
        GoodsCategories goodsCategories = goodsCategoriesRepository.findByGoodsId(goods).orElseThrow(() -> new ApiException(ErrorStatus._GOODS_CATEGORY_NOT_FOUND));
        Categories category = categoriesRepository.findById(goodsCategories.getCategoryId().getId()).orElseThrow(() -> new ApiException(ErrorStatus._CATEGORY_NOT_FOUND));
        return GoodsResponse.GoodsDetailResponseDTO.of(goods, category.getName());
    }

    // 상품 목록 조회 (+ 검색)
    @Override
    public GoodsResponse.GoodsListResponseDTO getGoodsList(Integer page, Integer size, String keyword) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Goods> goodsPage;

        if (keyword != null && !keyword.isEmpty()) {
            goodsPage = goodsRepository.findByNameContainingIgnoreCase(keyword, pageRequest);
        } else {
            goodsPage = goodsRepository.findAll(pageRequest);
        }

        List<GoodsResponse.GoodsResponseDTO> goodsDTOList = goodsPage.getContent().stream()
                .map(GoodsResponse.GoodsResponseDTO::from)
                .collect(Collectors.toList());

        return GoodsResponse.GoodsListResponseDTO.builder()
                .goodsDTOList(goodsDTOList)
                .build();
    }


    // 시드 값 조회
    @Override
    public GoodsResponse.GoodsSeedsResponseDTO getSeeds(Long goodsId) {
        Goods goods = goodsRepository.findById(goodsId).orElseThrow(() -> new ApiException(ErrorStatus._GOODS_NOT_FOUND));
        return GoodsResponse.GoodsSeedsResponseDTO.from(goods);
    }

    // 상품 검색
    @Override
    public GoodsResponse.GoodsListSearchResponseDTO searchGoods() {
        List<Goods> goodsList = goodsRepository.findAll();

        List<GoodsResponse.GoodsSearchResponseDTO> goodsSearchDTOList = goodsList.stream()
                .map(goods -> {
                    GoodsCategories goodsCategories = goodsCategoriesRepository.findByGoodsId(goods).orElseThrow(() -> new ApiException(ErrorStatus._GOODS_CATEGORY_NOT_FOUND));
                    Categories category = categoriesRepository.findById(goodsCategories.getCategoryId().getId()).orElseThrow(() -> new ApiException(ErrorStatus._CATEGORY_NOT_FOUND));
                    return GoodsResponse.GoodsSearchResponseDTO.of(goods, category.getName());
                })
                .collect(Collectors.toList());

        return GoodsResponse.GoodsListSearchResponseDTO.builder()
                .goodsSearchDTOList(goodsSearchDTOList)
                .build();
    }

}
