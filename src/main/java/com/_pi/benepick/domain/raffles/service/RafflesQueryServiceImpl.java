package com._pi.benepick.domain.raffles.service;

import com._pi.benepick.domain.categories.entity.Categories;
import com._pi.benepick.domain.categories.repository.CategoriesRepository;
import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goods.repository.GoodsRepository;
import com._pi.benepick.domain.goodsCategories.entity.GoodsCategories;
import com._pi.benepick.domain.goodsCategories.repository.GoodsCategoriesRepository;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.domain.raffles.dto.RafflesResponse;
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.domain.raffles.repository.RafflesRepository;
import com._pi.benepick.domain.winners.entity.Status;
import com._pi.benepick.domain.winners.entity.Winners;
import com._pi.benepick.domain.winners.repository.WinnersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RafflesQueryServiceImpl {

    private final RafflesRepository rafflesRepository;
    private final WinnersRepository winnersRepository;
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
                        Optional<GoodsCategories> optionalGoodsCategories = goodsCategoriesRepository.findByGoodsId(raffles.getGoodsId());

                        String categoryName = optionalGoodsCategories.map(goodsCategories -> goodsCategories.getCategoryId().getName()).orElse("NONE");

                        return RafflesResponse.RafflesResponseByMembersDTO.builder()
                                .id(raffles.getId())
                                .memberId(raffles.getMemberId().getId())
                                .goodsId(raffles.getGoodsId().getId())
                                .point(raffles.getPoint())
                                .rafflesAt(raffles.getUpdated_at())
                                .category_name(categoryName)
                                .build();
                    })
                    .toList();

            return RafflesResponse.RafflesResponseByMembersListDTO.builder()
                    .rafflesResponseByMembersList(rafflesResponseByMembersDTOS)
                    .build();
        } else {
            // MEMBERS_NOT_FOUND 로 변경 예정.
            throw new EntityNotFoundException("Members not found with id: " + memberId);
        }
    }

    public RafflesResponse.RafflesResponseByMembersListDTO getCompleteRafflesByMemberId(String memberId) {
        Optional<Members> membersOptional = membersRepository.findById(memberId);

        if (membersOptional.isPresent()) {
            Members members = membersOptional.get();
            List<Raffles> rafflesList = rafflesRepository.findAllByMemberId(members);

            List<RafflesResponse.RafflesResponseByMembersDTO> rafflesResponseByMembersDTOS = rafflesList.stream()
                    .filter(raffles -> raffles.getGoodsId().getGoodsStatus() == GoodsStatus.COMPLETED)
                    .map(raffles -> {
                        // 각 Raffles 객체의 id로 Winners 리스트 가져오기
                        Winners winners = winnersRepository.findByRaffleId(raffles).orElseThrow(() ->
                                new EntityNotFoundException("아직 종료되지 않은 응모 상품입니다.")
                        );

                        Optional<GoodsCategories> optionalGoodsCategories = goodsCategoriesRepository.findByGoodsId(raffles.getGoodsId());

                        String categoryName = optionalGoodsCategories.map(goodsCategories -> goodsCategories.getCategoryId().getName()).orElse("NONE");

                        return RafflesResponse.RafflesResponseByMembersDTO.builder()
                                .id(raffles.getId())
                                .memberId(raffles.getMemberId().getId())
                                .goodsId(raffles.getGoodsId().getId())
                                .point(raffles.getPoint())
                                .sequence(winners.getSequence())
                                .winnerStatus(winners.getStatus())
                                .rafflesAt(raffles.getUpdated_at())
                                .category_name(categoryName)
                                .build();
                    })
                    .collect(Collectors.toList());

            return RafflesResponse.RafflesResponseByMembersListDTO.builder()
                    .rafflesResponseByMembersList(rafflesResponseByMembersDTOS)
                    .build();
        } else {
            // MEMBERS_NOT_FOUND 로 변경 예정.
            throw new EntityNotFoundException("Members not found with id: " + memberId);
        }
    }

    public RafflesResponse.RafflesResponseByGoodsListDTO getAllRafflesByGoodsId(Long goodsId) {
        Optional<Goods> goodsOptional = goodsRepository.findById(goodsId);

        if (goodsOptional.isPresent()) {
            Goods goods = goodsOptional.get();
            List<Raffles> rafflesList = rafflesRepository.findAllByGoodsId(goods);

            List<RafflesResponse.RafflesResponseByGoodsDTO> rafflesResponseByGoodsDTOS = rafflesList.stream()
                    .map(raffles -> RafflesResponse.RafflesResponseByGoodsDTO.builder()
                            .id(raffles.getId())
                            .memberId(raffles.getMemberId().getId())
                            .memberName(raffles.getMemberId().getName())
                            .goodsId(raffles.getGoodsId().getId())
                            .point(raffles.getPoint())
                            .rafflesAt(raffles.getUpdated_at())
                            .build())
                    .collect(Collectors.toList());

            return RafflesResponse.RafflesResponseByGoodsListDTO.builder()
                    .rafflesResponseByGoodsList(rafflesResponseByGoodsDTOS)
                    .build();
        } else {
            // _GOODS_NOT_FOUND(HttpStatus.BAD_REQUEST 로 변경 예정.
            throw new EntityNotFoundException("Goods not found with id: " + goodsId);
        }
    }

    public RafflesResponse.RafflesResponseByGoodsListDTO getWaitlistRafflesByGoodsId(Long goodsId) {
        Optional<Goods> goodsOptional = goodsRepository.findById(goodsId);

        if (goodsOptional.isPresent()) {
            Goods goods = goodsOptional.get();
            List<Raffles> rafflesList = rafflesRepository.findAllByGoodsId(goods);

            List<RafflesResponse.RafflesResponseByGoodsDTO> rafflesResponseByGoodsDTOS = rafflesList.stream()
                    .map(raffles -> {
                        // 각 Raffles 객체의 id로 Winners 리스트 가져오기
                        Winners winners = winnersRepository.findByRaffleId(raffles).orElseThrow(() ->
                                new EntityNotFoundException("아직 종료되지 않은 응모 상품입니다.")
                        );

                        return new AbstractMap.SimpleEntry<>(raffles, winners);
                    })
                    .filter(entry -> entry.getValue().getStatus() == Status.WAITLIST)
                    .map(entry -> {
                        Raffles raffles = entry.getKey();
                        Winners winners = entry.getValue();

                        return RafflesResponse.RafflesResponseByGoodsDTO.builder()
                                .id(raffles.getId())
                                .memberId(raffles.getMemberId().getId())
                                .memberName(raffles.getMemberId().getName())
                                .point(raffles.getPoint())
                                .goodsId(raffles.getGoodsId().getId())
                                .sequence(winners.getSequence())
                                .winnerStatus(winners.getStatus())
                                .rafflesAt(raffles.getUpdated_at())
                                .build();
                    })
                    .collect(Collectors.toList());

            return RafflesResponse.RafflesResponseByGoodsListDTO.builder()
                    .rafflesResponseByGoodsList(rafflesResponseByGoodsDTOS)
                    .build();
        } else {
            // _GOODS_NOT_FOUND(HttpStatus.BAD_REQUEST 로 변경 예정.
            throw new EntityNotFoundException("Goods not found with id: " + goodsId);
        }
    }


    public RafflesResponse.RafflesResponseByGoodsListDTO getWinnersRafflesByGoodsId(Long goodsId) {
        Optional<Goods> goodsOptional = goodsRepository.findById(goodsId);

        if (goodsOptional.isPresent()) {
            Goods goods = goodsOptional.get();
            List<Raffles> rafflesList = rafflesRepository.findAllByGoodsId(goods);

            List<RafflesResponse.RafflesResponseByGoodsDTO> rafflesResponseByGoodsDTOS = rafflesList.stream()
                    .map(raffles -> {
                        // 각 Raffles 객체의 id로 Winners 리스트 가져오기
                        Winners winners = winnersRepository.findByRaffleId(raffles).orElseThrow(() ->
                                new EntityNotFoundException("아직 종료되지 않은 응모 상품입니다.")
                        );

                        return new AbstractMap.SimpleEntry<>(raffles, winners);
                    })
                    .filter(entry -> entry.getValue().getStatus() != Status.WAITLIST)
                    .map(entry -> {
                        Raffles raffles = entry.getKey();
                        Winners winners = entry.getValue();

                        return RafflesResponse.RafflesResponseByGoodsDTO.builder()
                                .id(raffles.getId())
                                .memberId(raffles.getMemberId().getId())
                                .memberName(raffles.getMemberId().getName())
                                .point(raffles.getPoint())
                                .goodsId(raffles.getGoodsId().getId())
                                .winnerStatus(winners.getStatus())
                                .rafflesAt(raffles.getUpdated_at())
                                .build();
                    })
                    .collect(Collectors.toList());

            return RafflesResponse.RafflesResponseByGoodsListDTO.builder()
                    .rafflesResponseByGoodsList(rafflesResponseByGoodsDTOS)
                    .build();
        } else {
            // _GOODS_NOT_FOUND(HttpStatus.BAD_REQUEST 로 변경 예정.
            throw new EntityNotFoundException("Goods not found with id: " + goodsId);
        }
    }
}