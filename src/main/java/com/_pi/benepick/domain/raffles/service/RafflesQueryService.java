package com._pi.benepick.domain.raffles.service;

import com._pi.benepick.domain.raffles.dto.RafflesResponse;

import java.util.List;

public interface RafflesQueryService {
    RafflesResponse.RafflesResponseByMembersListDTO getProgressRafflesByMemberId(String memberId); //상품 목록 조회
    RafflesResponse.RafflesResponseByGoodsListDTO getAllRafflesByGoodsId(Long goodsId); //상품 목록 조회
}
