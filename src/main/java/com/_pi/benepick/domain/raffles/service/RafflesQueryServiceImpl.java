package com._pi.benepick.domain.raffles.service;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.domain.raffles.repository.RafflesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RafflesQueryServiceImpl implements RafflesQueryService {

    private final RafflesRepository rafflesRepository;

    public List<Raffles> findAllByMemberId(Members members) {
        return rafflesRepository.findAllByMemberId(members);
    }

    public List<Raffles> findAllByGoodsId(Goods goods) {
        return rafflesRepository.findAllByGoodsId(goods);
    }

    public List<Raffles> findAllByGoodsIdOrderByPointAsc(Goods goods) {
        return rafflesRepository.findAllByGoodsIdOrderByPointAsc(goods);
    }

}