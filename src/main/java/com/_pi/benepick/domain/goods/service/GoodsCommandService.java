package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.goods.dto.GoodsRequest;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;

import java.time.LocalDateTime;

public interface GoodsCommandService {
    GoodsStatus determineGoodsStatus(LocalDateTime raffleStartAt, LocalDateTime raffleEndAt);
    Goods createGoods(GoodsRequest.GoodsRequestDTO goodsAddDTO);
}
