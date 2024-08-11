package com._pi.benepick.domain.raffles.service;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.raffles.dto.RafflesRequest;
import com._pi.benepick.domain.raffles.dto.RafflesResponse;


public interface RafflesQueryService {
    RafflesResponse.RafflesResponseByMembersListDTO getProgressRafflesByMemberId(Members member); //상품 목록 조회
    RafflesResponse.RafflesResponseByGoodsListDTO getAllRafflesByGoodsId(Members members, Long goodsId); //상품 목록 조회
    RafflesResponse.CurrentStateByGoodsListDTO getCurrentStateByGoods(Long goodsId);
}
