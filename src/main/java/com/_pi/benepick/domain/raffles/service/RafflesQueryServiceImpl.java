package com._pi.benepick.domain.raffles.service;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goods.service.GoodsQueryService;
import com._pi.benepick.domain.goodsCategories.service.GoodsCategoriesQueryService;
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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RafflesQueryServiceImpl implements RafflesQueryService {

    private final RafflesRepository rafflesRepository;
    private final GoodsQueryService goodsQueryService;
    private final GoodsCategoriesQueryService goodsCategoriesQueryService;

    public RafflesResponse.RafflesResponseByMembersListDTO getProgressRafflesByMemberId(Members member) {
        if(member.getRole().equals(Role.ADMIN)) throw new ApiException(ErrorStatus._UNAUTHORIZED);

        List<Raffles> rafflesList = rafflesRepository.findAllByMemberId(member);

        List<RafflesResponse.RafflesResponseByMembersDTO> rafflesResponseByMembersDTOS = rafflesList.stream()
            .filter(raffles -> raffles.getGoodsId().getGoodsStatus() == GoodsStatus.PROGRESS)
            .map(raffles -> {
                String categoryName = goodsCategoriesQueryService.getGoodsCategory(raffles);

                return RafflesResponse.RafflesResponseByMembersDTO.of(raffles, categoryName);
            })
            .toList();

        return RafflesResponse.RafflesResponseByMembersListDTO.builder()
            .rafflesResponseByMembersList(rafflesResponseByMembersDTOS)
            .build();

    }

    public RafflesResponse.RafflesResponseByGoodsListDTO getAllRafflesByGoodsId(Members members, Long goodsId) {
        if (!(members.getRole().equals(Role.ADMIN))) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        List<RafflesResponse.RafflesResponseByGoodsDTO> rafflesResponseByGoodsDTOS = rafflesRepository
                .findAllByGoodsId(goodsQueryService.goodsFindById(goodsId)).stream()
                .map(RafflesResponse.RafflesResponseByGoodsDTO::from)
            .toList();

        return RafflesResponse.RafflesResponseByGoodsListDTO.builder()
                .rafflesResponseByGoodsList(rafflesResponseByGoodsDTOS)
                .build();
    }

    public List<Raffles> findAllByGoodsIdOrderByPointAsc(Goods goods) {
        return rafflesRepository.findAllByGoodsIdOrderByPointAsc(goods);
    }

}