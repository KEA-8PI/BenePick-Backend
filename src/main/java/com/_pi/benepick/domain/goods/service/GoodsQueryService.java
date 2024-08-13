package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsFilter;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.members.entity.Members;

import java.time.LocalDateTime;
import java.util.List;

public interface GoodsQueryService {
    GoodsResponse.GoodsListResponseDTO getGoodsList(Integer page, Integer size, String keyword, Members member); //상품 목록 조회
    GoodsResponse.GoodsDetailResponseDTO getGoodsInfo(Long goodsId); // 상품 상세 조회
    GoodsResponse.GoodsSeedsResponseDTO getSeeds(Long goodsId); //시드 값 조회
    GoodsResponse.GoodsListSearchResponseDTO searchGoods(GoodsStatus goodsStatus, Integer page, Integer size, String keyword, GoodsFilter sortBy, String category, Members member); //상품 검색
    Goods findById(Long goodsId);
    List<Goods> findByRaffleEndAtBeforeAndGoodsStatus(LocalDateTime now);
}