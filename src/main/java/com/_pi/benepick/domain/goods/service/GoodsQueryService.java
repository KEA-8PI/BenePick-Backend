package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.goods.dto.GoodsResponse;

public interface GoodsQueryService {
    GoodsResponse.GoodsResponseDTO getGoodsInfo(Long goodsId); // 상품 상세 조회
}
