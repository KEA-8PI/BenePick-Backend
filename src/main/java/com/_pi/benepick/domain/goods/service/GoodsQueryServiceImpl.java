package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.goods.dto.GoodsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsQueryServiceImpl implements GoodsQueryService{

    // 상품 상세 조회
    @Override
    public GoodsResponse.GoodsResponseDTO getGoodsInfo(Long goodsId) {

    }


}
