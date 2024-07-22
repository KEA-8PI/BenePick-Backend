package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.goods.dto.GoodsResponse;

import java.util.List;

public interface GoodsQueryService {
    GoodsResponse.GoodsResponseDTO getGoodsInfo(Long goodsId); // 상품 상세 조회
    List getGoodsList(); //상품 목록 조회
}