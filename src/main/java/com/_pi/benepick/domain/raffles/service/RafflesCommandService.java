package com._pi.benepick.domain.raffles.service;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.raffles.entity.Raffles;

public interface RafflesCommandService {
    Raffles findRaffleByGoodsIdAndMemberId(Goods goods, Members members, Long point);
}
