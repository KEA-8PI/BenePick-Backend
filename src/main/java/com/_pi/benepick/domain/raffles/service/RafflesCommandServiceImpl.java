package com._pi.benepick.domain.raffles.service;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.raffles.dto.RafflesRequest.RafflesRequestDTO;
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

    public Raffles fetchOrInitializeRaffle(Goods goods, Members members, Long point) {
        Raffles raffles = rafflesRepository.findByGoodsIdAndMemberId(goods, members).orElse(RafflesRequestDTO
                .toEntity(members, goods, 'F'));
        raffles.increasePoint(point);
        rafflesRepository.save(raffles);
        return raffles;
    }
}
