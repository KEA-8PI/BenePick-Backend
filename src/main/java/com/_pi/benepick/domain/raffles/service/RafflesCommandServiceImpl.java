package com._pi.benepick.domain.raffles.service;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.raffles.dto.RafflesRequest;
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.domain.raffles.repository.RafflesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RafflesCommandServiceImpl implements RafflesCommandService{

    private final RafflesRepository rafflesRepository;

    public Raffles findRaffleByGoodsIdAndMemberId(Goods goods, Members members, Long point) {
        Raffles raffles = rafflesRepository.findByGoodsIdAndMemberId(goods, members).orElse(RafflesRequest.RafflesRequestDTO
                .toEntity(members, goods, 'F'));
        raffles.increasePoint(point);
        return raffles;
    }
}
