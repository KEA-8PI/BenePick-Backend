package com._pi.benepick.domain.raffles.service;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.raffles.dto.RafflesResponse.RafflesResponseByMembersListDTO;
import com._pi.benepick.domain.raffles.entity.Raffles;

import java.util.List;

public interface RafflesQueryService {
    List<Raffles> findAllByGoodsIdOrderByPointAsc(Goods goods);

    List<Raffles> findAllByGoodsId(Goods goods);
    List<Raffles> findAllByGoodsIdOrderByPointDesc(Goods goods);
<<<<<<< HEAD
    RafflesResponseByMembersListDTO getProgressRafflesByMemberId(Members member);
=======
    RafflesResponse.RafflesResponseByMembersListDTO getProgressRafflesByMemberId(Members member);

>>>>>>> 35df897ece953f76e3842c693a9ba4cd133a65df
}
