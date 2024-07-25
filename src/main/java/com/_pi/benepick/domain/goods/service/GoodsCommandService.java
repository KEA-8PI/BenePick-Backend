package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.goods.dto.GoodsRequest;
import com._pi.benepick.domain.goods.dto.GoodsRequest.GoodsDeleteRequestDTO;
import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.global.common.response.ApiResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface GoodsCommandService {
    //상품 엑셀 파일로 추가
    GoodsResponse.GoodsResponseDTO addGoods(GoodsRequest.GoodsRequestDTO goodsAddDTO); //상품 추가
    GoodsResponse.GoodsResponseDTO updateGoods(Long goodsId, GoodsRequest.GoodsRequestDTO goodsUpdateDTO); //상품 수정
    String deleteGoods(GoodsDeleteRequestDTO goodsDeleteRequestDTO); //상품 삭제
}
