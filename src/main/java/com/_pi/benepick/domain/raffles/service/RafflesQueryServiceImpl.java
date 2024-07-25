package com._pi.benepick.domain.raffles.service;

import com._pi.benepick.domain.raffles.dto.RafflesResponse;
import com._pi.benepick.domain.raffles.repository.RafflesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RafflesQueryServiceImpl {

    private final RafflesRepository rafflesRepository;

    @Autowired
    public RafflesQueryServiceImpl(RafflesRepository rafflesRepository) {
        this.rafflesRepository = rafflesRepository;
    }

    public List<RafflesResponse.RafflesResponseByGoodsDTO> getAllRafflesByGoodsId(Long goodsId) {
        return rafflesRepository.findAllByGoodsId(goodsId);
    }

    public List<RafflesResponse.RafflesResponseByMembersDTO> getAllRafflesByMemberId(String memberId) {
        return rafflesRepository.findAllByMemberId(memberId);
    }
}