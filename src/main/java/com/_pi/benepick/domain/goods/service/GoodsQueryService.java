package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.goods.dto.GoodsResponse;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GoodsQueryService {
    GoodsResponse.GoodsDetailResponseDTO getGoodsInfo(Long goodsId); // 상품 상세 조회
    GoodsResponse.GoodsListResponseDTO getGoodsList(Integer page, Integer size, String keyword); //상품 목록 조회
    GoodsResponse.GoodsSeedsResponseDTO getSeeds(Long goodsId); //시드 값 조회
    GoodsResponse.GoodsListSearchResponseDTO searchGoods(); //상품 검색
}