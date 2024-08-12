package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.goods.dto.GoodsRequest;
import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.members.entity.Members;

import java.time.LocalDateTime;
import java.util.List;

public interface GoodsCommandService {
    GoodsStatus determineGoodsStatus(LocalDateTime raffleStartAt, LocalDateTime raffleEndAt);
    Goods createGoods(GoodsRequest.GoodsRequestDTO goodsAddDTO);
}
