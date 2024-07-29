package com._pi.benepick.domain.winners.service;

import com._pi.benepick.domain.raffles.dto.RafflesResponse;
import com._pi.benepick.domain.winners.dto.WinnersResponse;
import com._pi.benepick.domain.winners.entity.Winners;

import java.util.List;

public interface WinnersQueryService {
    WinnersResponse.WinnersResponseByGoodsListDTO getWaitlistByGoodsId(Long goodsId);
    WinnersResponse.WinnersResponseByGoodsListDTO getWinnersByGoodsId(Long goodsId);
    WinnersResponse.WinnersResponseByMembersListDTO getCompleteRafflesByMemberId(String memberId);
}
