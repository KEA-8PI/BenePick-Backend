package com._pi.benepick.domain.raffles.service;

import com._pi.benepick.domain.raffles.dto.RafflesRequest;
import com._pi.benepick.domain.raffles.dto.RafflesResponse;
import com._pi.benepick.domain.raffles.entity.Raffles;

public interface RafflesCommandService {
    RafflesResponse.ApplyRafflesResponseByGoodsId applyRaffle(String memberId, Long goodsId, RafflesRequest.RafflesRequestDTO raffleAddDTO);
}
