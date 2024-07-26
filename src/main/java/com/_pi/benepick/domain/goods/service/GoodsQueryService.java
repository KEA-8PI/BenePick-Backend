package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.goods.dto.GoodsResponse;

import java.util.List;

public interface GoodsQueryService {
    GoodsResponse.GoodsResponseDTO getGoodsInfo(Long goodsId); // 상품 상세 조회
    GoodsResponse.GoodsListResponseDTO getGoodsList(); //상품 목록 조회
    GoodsResponse.GoodsSeedsResponseDTO getSeeds(Long goodsId); //시드 값 조회
}