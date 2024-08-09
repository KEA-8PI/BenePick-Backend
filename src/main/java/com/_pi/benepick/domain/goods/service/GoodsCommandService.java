package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.goods.dto.GoodsRequest;
import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.members.entity.Members;
import java.util.List;

public interface GoodsCommandService {
    Goods addGoods(GoodsRequest.GoodsRequestDTO goodsAddDTO, Members member); //상품 추가
    GoodsResponse.GoodsAddResponseDTO updateGoods(Long goodsId, GoodsRequest.GoodsRequestDTO goodsUpdateDTO, Members member); //상품 수정
    GoodsResponse.GoodsDeleteResponseDTO deleteGoods(List<Long> goodsDeleteRequestDTO, Members member); //상품 삭제
}
