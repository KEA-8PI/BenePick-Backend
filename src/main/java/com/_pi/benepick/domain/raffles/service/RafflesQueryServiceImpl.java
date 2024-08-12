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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RafflesQueryServiceImpl implements RafflesQueryService {

    private final RafflesRepository rafflesRepository;
    private final GoodsRepository goodsRepository;
    private final MembersRepository membersRepository;
    private final GoodsCategoriesRepository goodsCategoriesRepository;

    public RafflesResponse.RafflesResponseByMembersListDTO getProgressRafflesByMemberId(Members member) {
        if(member.getRole().equals(Role.ADMIN)) throw new ApiException(ErrorStatus._UNAUTHORIZED);

        List<Raffles> rafflesList = rafflesRepository.findAllByMemberId(member);

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

    }

    public RafflesResponse.RafflesResponseByGoodsListDTO getAllRafflesByGoodsId(Members members, Long goodsId) {
        if (!(members.getRole().equals(Role.ADMIN))) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        Optional<Goods> goodsOptional = goodsRepository.findById(goodsId);

        if (goodsOptional.isPresent()) {
            Goods goods = goodsOptional.get();
            List<Raffles> rafflesList = rafflesRepository.findAllByGoodsId(goods);

            List<RafflesResponse.RafflesResponseByGoodsDTO> rafflesResponseByGoodsDTOS = rafflesList.stream()
                    .map(RafflesResponse.RafflesResponseByGoodsDTO::from)
                .toList();

            return RafflesResponse.RafflesResponseByGoodsListDTO.builder()
                    .rafflesResponseByGoodsList(rafflesResponseByGoodsDTOS)
                    .build();
        } else {
            throw new ApiException(ErrorStatus._GOODS_NOT_FOUND);
        }
    }

    public RafflesResponse.CurrentStateByGoodsListDTO getCurrentStateByGoods(Long goodsId) {
        List<Raffles> rafflesList = rafflesRepository.findAllByGoodsIdOrderByPointDesc(
                goodsRepository.findById(goodsId).orElseThrow(() -> new ApiException(ErrorStatus._GOODS_NOT_FOUND)));

        List<RafflesResponse.CurrentStateByGoodsDTO> currentStateByGoodsDTOS = new ArrayList<>();
        int limit = Math.min(5, rafflesList.size());
        for (int i = 0; i < limit; i++) {
            currentStateByGoodsDTOS.add(RafflesResponse.CurrentStateByGoodsDTO.builder()
                            .grade(i + 1)
                            .point(rafflesList.get(i).getPoint())
                            .build());
        }

        if (rafflesList.size() > 5) {
            Long totalRemainingPoints = rafflesList.subList(5, rafflesList.size())
                    .stream()
                    .mapToLong(Raffles::getPoint)
                    .sum();

            currentStateByGoodsDTOS.add(RafflesResponse.CurrentStateByGoodsDTO.builder()
                    .grade(6)
                    .point(totalRemainingPoints)
                    .build());
        }

        Long total = pointTotal(rafflesList);
        return RafflesResponse.CurrentStateByGoodsListDTO.builder()
                .currentStateByGoodsDTOList(currentStateByGoodsDTOS)
                .average(Math.round((float) total / rafflesList.size()))
                .total(total)
                .build();
    }

    private Long pointTotal(List<Raffles> rafflesList) {
        Long total = 0L;
        for (Raffles raffles : rafflesList) {
            total += raffles.getPoint();
        }
        return total;
    }
}