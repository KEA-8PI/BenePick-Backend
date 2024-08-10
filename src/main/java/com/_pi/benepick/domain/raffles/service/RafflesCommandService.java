package com._pi.benepick.domain.raffles.service;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.raffles.dto.RafflesRequest;
import com._pi.benepick.domain.raffles.dto.RafflesResponse;

public interface RafflesCommandService {
    RafflesResponse.RafflesResponseByGoodsDTO applyRaffle(Members member, Long goodsId, RafflesRequest.RafflesRequestDTO raffleAddDTO);
}
