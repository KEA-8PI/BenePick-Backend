package com._pi.benepick.domain.winners.service;

import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goodsCategories.repository.GoodsCategoriesRepository;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.raffles.dto.RafflesResponse;
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.domain.winners.dto.WinnersResponse;
import com._pi.benepick.domain.winners.entity.Status;
import com._pi.benepick.domain.winners.entity.Winners;
import com._pi.benepick.domain.winners.repository.WinnersRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WinnersQueryServiceImpl implements WinnersQueryService {

    private final WinnersRepository winnersRepository;
    private final GoodsCategoriesRepository goodsCategoriesRepository;

    public WinnersResponse.WinnersResponseByGoodsListDTO getWaitlistByGoodsId(Long goodsId) {
        List<WinnersResponse.WinnersResponseByGoodsDTO> winnersResponseByGoodsDTOS = (winnersRepository.findByGoodsId(goodsId)).stream()
                .filter(winners -> winners.getStatus() == Status.WAITLIST)
                .map(winners -> WinnersResponse.WinnersResponseByGoodsDTO.builder()
                        .id(winners.getRaffleId().getId())
                        .memberId(winners.getRaffleId().getMemberId().getId())
                        .memberName(winners.getRaffleId().getMemberId().getName())
                        .goodsId(winners.getRaffleId().getGoodsId().getId())
                        .point(winners.getRaffleId().getPoint())
                        .sequence(winners.getSequence())
                        .winnerStatus(winners.getStatus())
                        .rafflesAt(winners.getRaffleId().getUpdated_at())
                        .build())
                .collect(Collectors.toList());

        return WinnersResponse.WinnersResponseByGoodsListDTO.builder()
                .winnersResponseByGoodsDTOList(winnersResponseByGoodsDTOS)
                .build();
    }

    public WinnersResponse.WinnersResponseByGoodsListDTO getWinnersByGoodsId(Long goodsId) {
        List<WinnersResponse.WinnersResponseByGoodsDTO> winnersResponseByGoodsDTOS = (winnersRepository.findByGoodsId(goodsId)).stream()
                .filter(winners -> winners.getStatus() != Status.WAITLIST)
                .map(winners -> WinnersResponse.WinnersResponseByGoodsDTO.builder()
                        .id(winners.getRaffleId().getId())
                        .memberId(winners.getRaffleId().getMemberId().getId())
                        .memberName(winners.getRaffleId().getMemberId().getName())
                        .goodsId(winners.getRaffleId().getGoodsId().getId())
                        .point(winners.getRaffleId().getPoint())
                        .winnerStatus(winners.getStatus())
                        .rafflesAt(winners.getRaffleId().getUpdated_at())
                        .build())
                .collect(Collectors.toList());

        return WinnersResponse.WinnersResponseByGoodsListDTO.builder()
                .winnersResponseByGoodsDTOList(winnersResponseByGoodsDTOS)
                .build();
    }

    public WinnersResponse.WinnersResponseByMembersListDTO getCompleteRafflesByMemberId(String memberId) {
        List<WinnersResponse.WinnersResponseByMembersDTO> winnersResponseByMembersDTOS = (winnersRepository.findByMemberId(memberId)).stream()
                .filter(winners -> winners.getRaffleId().getGoodsId().getGoodsStatus() == GoodsStatus.COMPLETED)
                .map(winners -> {
                    String categoryName = (goodsCategoriesRepository.findByGoodsId(winners.getRaffleId().getGoodsId())).map(goodsCategories -> goodsCategories.getCategoryId().getName()).orElse("NONE");

                    return WinnersResponse.WinnersResponseByMembersDTO.builder()
                            .id(winners.getRaffleId().getId())
                            .memberId(winners.getRaffleId().getMemberId().getId())
                            .goodsId(winners.getRaffleId().getGoodsId().getId())
                            .point(winners.getRaffleId().getPoint())
                            .sequence(winners.getSequence())
                            .winnerStatus(winners.getStatus())
                            .rafflesAt(winners.getRaffleId().getUpdated_at())
                            .category_name(categoryName)
                            .build();
                })
                .collect(Collectors.toList());

        return WinnersResponse.WinnersResponseByMembersListDTO.builder()
                .winnersResponseByMembersList(winnersResponseByMembersDTOS)
                .build();
    }


}
