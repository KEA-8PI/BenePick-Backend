package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.goods.dto.GoodsRequest;
import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.members.entity.Members;

public interface GoodsCommandService {
    GoodsResponse.GoodsAddResponseDTO addGoods(GoodsRequest.GoodsRequestDTO goodsAddDTO, Members member); //상품 추가
    GoodsResponse.GoodsAddResponseDTO updateGoods(Long goodsId, GoodsRequest.GoodsRequestDTO goodsUpdateDTO, Members member); //상품 수정
}
