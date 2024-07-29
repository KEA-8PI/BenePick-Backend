package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.goods.dto.GoodsRequest;
import com._pi.benepick.domain.goods.dto.GoodsResponse;
import java.util.List;

public interface GoodsCommandService {
    //상품 엑셀 파일로 추가
    GoodsResponse.GoodsAddResponseDTO addGoods(GoodsRequest.GoodsRequestDTO goodsAddDTO); //상품 추가
    GoodsResponse.GoodsAddResponseDTO updateGoods(Long goodsId, GoodsRequest.GoodsRequestDTO goodsUpdateDTO); //상품 수정
    GoodsResponse.GoodsDeleteResponseDTO deleteGoods(List<Long> goodsDeleteRequestDTO); //상품 삭제
}
