package com._pi.benepick.domain.raffles.service;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.raffles.dto.RafflesResponse;
import com._pi.benepick.domain.raffles.entity.Raffles;

import java.util.List;

public interface RafflesQueryService {
    RafflesResponse.RafflesResponseByMembersListDTO getProgressRafflesByMemberId(Members member); //상품 목록 조회
    RafflesResponse.RafflesResponseByGoodsListDTO getAllRafflesByGoodsId(Members members, Long goodsId); //상품 목록 조회
    List<Raffles> findAllByGoodsIdOrderByPointAsc(Goods goods);
}
