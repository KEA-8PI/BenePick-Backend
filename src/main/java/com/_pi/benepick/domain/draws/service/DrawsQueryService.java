package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.draws.entity.Status;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.members.entity.Members;

import java.util.List;

public interface DrawsQueryService {
    Draws findDrawsById(Long drawsId);
    List<Draws> findAllByGoodsIdAndStatus(Goods goods, Status status);
    List<Draws> findByGoodsId(Long goodsId);
    List<Draws> findByMemberId(Members member);
}
