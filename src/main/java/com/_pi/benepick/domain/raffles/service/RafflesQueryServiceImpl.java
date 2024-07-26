package com._pi.benepick.domain.raffles.service;

import com._pi.benepick.domain.categories.entity.Categories;
import com._pi.benepick.domain.categories.repository.CategoriesRepository;
import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.goods.entity.Goods;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RafflesQueryServiceImpl {

    private RafflesRepository rafflesRepository;
    private WinnersRepository winnersRepository;
    private GoodsRepository goodsRepository;
    private MembersRepository membersRepository;
    private GoodsCategoriesRepository goodsCategoriesRepository;

    @Autowired
    public RafflesQueryServiceImpl(RafflesRepository rafflesRepository, WinnersRepository winnersRepository, GoodsRepository goodsRepository, MembersRepository membersRepository, GoodsCategoriesRepository goodsCategoriesRepository) {
        this.rafflesRepository = rafflesRepository;
        this.winnersRepository = winnersRepository;
        this.goodsRepository = goodsRepository;
        this.membersRepository = membersRepository;
        this.goodsCategoriesRepository = goodsCategoriesRepository;
    }

    public RafflesResponse.RafflesResponseByGoodsListDTO getAllRafflesByGoodsId(Long goodsId) {
        Optional<Goods> goodsOptional = goodsRepository.findById(goodsId);

        if (goodsOptional.isPresent()) {
            Goods goods = goodsOptional.get();
            List<Raffles> rafflesList = rafflesRepository.findAllByGoodsId(goods);

            List<RafflesResponse.RafflesResponseByGoodsDTO> rafflesResponseByGoodsDTOS = rafflesList.stream()
                    .map(raffles -> {
                        // 각 Raffles 객체의 id로 Winners 리스트 가져오기
                        Optional<Winners> winnersOptional = (winnersRepository.findByRaffleId(raffles));

                        Winners winners = winnersOptional.orElseGet(() -> {
                            Winners defaultWinners = new Winners();
                            defaultWinners.setSequence(-1);
                            defaultWinners.setStatus(Status.SCHEDULED);
                            return defaultWinners;
                        });

                        return RafflesResponse.RafflesResponseByGoodsDTO.builder()
                                .id(raffles.getId())
                                .memberId(raffles.getMemberId().getId())
                                .memberName(raffles.getMemberId().getName())
                                .goodsId(raffles.getGoodsId().getId())
                                .point(raffles.getPoint())
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

    public RafflesResponse.RafflesResponseByMembersListDTO getAllRafflesByMemberId(String memberId) {
        Optional<Members> membersOptional = membersRepository.findById(memberId);

        if (membersOptional.isPresent()) {
            Members members = membersOptional.get();
            List<Raffles> rafflesList = rafflesRepository.findAllByMemberId(members);

            List<RafflesResponse.RafflesResponseByMembersDTO> rafflesResponseByMembersDTOS = rafflesList.stream()
                    .map(raffles -> {
                        // 각 Raffles 객체의 id로 Winners 리스트 가져오기
                        Winners winners = (winnersRepository.findByRaffleId(raffles)).orElseGet(() -> {
                            Winners defaultWinners = new Winners();
                            defaultWinners.setSequence(-1);
                            defaultWinners.setStatus(Status.SCHEDULED);
                            return defaultWinners;
                        });

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
                    .toList();

            return RafflesResponse.RafflesResponseByMembersListDTO.builder()
                    .rafflesResponseByMembersList(rafflesResponseByMembersDTOS)
                    .build();
        } else {
            // MEMBERS_NOT_FOUND 로 변경 예정.
            throw new EntityNotFoundException("Members not found with id: " + memberId);
        }
    }
}