package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsFilter;
import com._pi.benepick.domain.members.entity.Members;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import java.time.LocalDateTime;
import java.util.List;

public interface GoodsQueryService {
    GoodsResponse.GoodsListResponseDTO getGoodsList(Integer page, Integer size, String keyword, Members member); //상품 목록 조회
    GoodsResponse.GoodsSeedsResponseDTO getSeeds(Long goodsId); //시드 값 조회
    PageRequest createPageRequest(Integer page, Integer size, GoodsFilter sortBy);
    Goods getGoodsById(Long id);
    List<Goods> findByRaffleEndAtBeforeAndGoodsStatus(LocalDateTime now);
}