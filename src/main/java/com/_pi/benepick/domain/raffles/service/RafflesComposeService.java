package com._pi.benepick.domain.raffles.service;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.raffles.dto.RafflesRequest;
import com._pi.benepick.domain.raffles.dto.RafflesResponse;

public interface RafflesComposeService {
    RafflesResponse.RafflesResponseByGoodsDTO applyRaffle(Members members, Long goodsId, RafflesRequest.RafflesRequestDTO raffleAddDTO);
    RafflesResponse.RafflesResponseByGoodsListDTO getAllRafflesByGoodsId(Members members, Long goodsId);
    RafflesResponse.CurrentStateByGoodsListDTO getCurrentStateByGoods(Long goodsId);
}
