package com._pi.benepick.domain.raffles.service;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.domain.raffles.dto.RafflesResponse;
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.domain.raffles.repository.RafflesRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RafflesQueryServiceImpl implements RafflesQueryService {

    private final RafflesRepository rafflesRepository;

    public List<Raffles> findAllByGoodsId(Goods goods) {
        return rafflesRepository.findAllByGoodsId(goods);
    }

    public List<Raffles> findAllByGoodsIdOrderByPointAsc(Goods goods) {
        return rafflesRepository.findAllByGoodsIdOrderByPointAsc(goods);
    }

    public List<Raffles> findAllByGoodsIdOrderByPointDesc(Goods goods) {
        return rafflesRepository.findAllByGoodsIdOrderByPointDesc(goods);
    }

    public RafflesResponse.RafflesResponseByMembersListDTO getProgressRafflesByMemberId(Members member) {
        if(member.getRole().equals(Role.ADMIN)) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        List<RafflesResponse.RafflesAndGoodsCategory> rafflesList = rafflesRepository.findRafflsAndGoodsCategoryByMemberId(member.getId());

        List<RafflesResponse.RafflesResponseByMembersDTO> rafflesResponseByMembersDTOS = rafflesList.stream()
                .filter(raffles -> raffles.getRaffles().getGoodsId().getGoodsStatus() == GoodsStatus.PROGRESS)
                .map(raffles -> RafflesResponse.RafflesResponseByMembersDTO.of(raffles.getRaffles(), raffles.getCategory()))
                .toList();

        return RafflesResponse.RafflesResponseByMembersListDTO.builder()
                .rafflesResponseByMembersList(rafflesResponseByMembersDTOS)
                .build();
    }
}