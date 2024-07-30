package com._pi.benepick.domain.raffles.service;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goods.repository.GoodsRepository;
import com._pi.benepick.domain.goodsCategories.repository.GoodsCategoriesRepository;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.domain.raffles.dto.RafflesRequest;
import com._pi.benepick.domain.raffles.dto.RafflesResponse;
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.domain.raffles.repository.RafflesRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RafflesQueryServiceImpl implements RafflesQueryService {

    private final RafflesRepository rafflesRepository;
    private final GoodsRepository goodsRepository;
    private final MembersRepository membersRepository;
    private final GoodsCategoriesRepository goodsCategoriesRepository;

    public RafflesResponse.RafflesResponseByMembersListDTO getProgressRafflesByMemberId(String memberId) {
        Optional<Members> membersOptional = membersRepository.findById(memberId);

        if (membersOptional.isPresent()) {
            Members members = membersOptional.get();
            List<Raffles> rafflesList = rafflesRepository.findAllByMemberId(members);

            List<RafflesResponse.RafflesResponseByMembersDTO> rafflesResponseByMembersDTOS = rafflesList.stream()
                    .filter(raffles -> raffles.getGoodsId().getGoodsStatus() == GoodsStatus.PROGRESS)
                    .map(raffles -> {
                        String categoryName = (goodsCategoriesRepository.findByGoodsId(raffles.getGoodsId()))
                                .map(goodsCategories -> goodsCategories.getCategoryId().getName()).orElse("NONE");

                        return RafflesResponse.RafflesResponseByMembersDTO.of(raffles, categoryName);
                    })
                    .toList();

            return RafflesResponse.RafflesResponseByMembersListDTO.builder()
                    .rafflesResponseByMembersList(rafflesResponseByMembersDTOS)
                    .build();
        } else {
            throw new ApiException(ErrorStatus._UNAUTHORIZED);
        }
    }

    public RafflesResponse.RafflesResponseByGoodsListDTO getAllRafflesByGoodsId(Members members, Long goodsId) {
        if (!(members.getRole().equals(Role.ADMIN))) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        Optional<Goods> goodsOptional = goodsRepository.findById(goodsId);

        if (goodsOptional.isPresent()) {
            Goods goods = goodsOptional.get();
            List<Raffles> rafflesList = rafflesRepository.findAllByGoodsId(goods);

            List<RafflesResponse.RafflesResponseByGoodsDTO> rafflesResponseByGoodsDTOS = rafflesList.stream()
                    .map(RafflesResponse.RafflesResponseByGoodsDTO::from)
                    .collect(Collectors.toList());

            return RafflesResponse.RafflesResponseByGoodsListDTO.builder()
                    .rafflesResponseByGoodsList(rafflesResponseByGoodsDTOS)
                    .build();
        } else {
            throw new ApiException(ErrorStatus._GOODS_NOT_FOUND);
        }
    }

    public RafflesResponse.RafflesResponseByGoodsDTO applyRaffle(String memberId, Long goodsId, RafflesRequest.RafflesRequestDTO raffleAddDTO) {
        Goods goods = goodsRepository.findById(goodsId).orElseThrow(() -> new ApiException(ErrorStatus._GOODS_NOT_FOUND));
        Members members = membersRepository.findById(memberId).orElseThrow(() -> new ApiException(ErrorStatus._UNAUTHORIZED));

        Raffles raffles = raffleAddDTO.toEntity(members, goods);
        Raffles savedRaffles = rafflesRepository.save(raffles);

        return RafflesResponse.RafflesResponseByGoodsDTO.from(savedRaffles);
    }
}