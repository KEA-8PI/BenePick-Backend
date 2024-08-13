package com._pi.benepick.domain.raffles.service;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.raffles.dto.RafflesRequest.RafflesRequestDTO;
import com._pi.benepick.domain.raffles.dto.RafflesResponse.*;

public interface RafflesComposeService {
    RafflesResponseByGoodsDTO applyRaffle(Members members, Long goodsId, RafflesRequestDTO raffleAddDTO);
    RafflesResponseByGoodsListDTO getAllRafflesByGoodsId(Members members, Long goodsId);
    CurrentStateByGoodsListDTO getCurrentStateByGoods(Long goodsId);
}
