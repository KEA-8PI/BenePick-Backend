package com._pi.benepick.domain.raffles.service;

import com._pi.benepick.domain.raffles.dto.RafflesResponse;
import com._pi.benepick.domain.raffles.repository.RafflesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RafflesQueryServiceImpl {

    private RafflesRepository rafflesRepository;

    @Autowired
    public RafflesQueryServiceImpl(RafflesRepository rafflesRepository) {
        this.rafflesRepository = rafflesRepository;
    }

    public RafflesResponse.RafflesResponseByGoodsListDTO getAllRafflesByGoodsId(Long goodsId) {
        List<RafflesResponse.RafflesResponseByGoodsDTO> rafflesResponseByGoodsDTOS = rafflesRepository.findAllByGoodsId(goodsId);
        return new RafflesResponse.RafflesResponseByGoodsListDTO(rafflesResponseByGoodsDTOS);
    }

    public RafflesResponse.RafflesResponseByMembersListDTO getAllRafflesByMemberId(String memberId) {
        List<RafflesResponse.RafflesResponseByMembersDTO> rafflesResponseByMembersDTOS = rafflesRepository.findAllByMemberId(memberId);
        return new RafflesResponse.RafflesResponseByMembersListDTO(rafflesResponseByMembersDTOS);
    }
}