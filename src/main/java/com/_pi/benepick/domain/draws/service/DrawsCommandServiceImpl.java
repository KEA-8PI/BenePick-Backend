package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.dto.DrawsRequest;
import com._pi.benepick.domain.draws.dto.DrawsResponse;
import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.draws.entity.Status;
import com._pi.benepick.domain.draws.repository.DrawsRepository;
import com._pi.benepick.domain.draws.service.algorithm.RaffleDraw;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.service.GoodsQueryService;
import com._pi.benepick.domain.hash.entity.Hash;
import com._pi.benepick.domain.hash.service.HashQueryService;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.domain.penaltyHists.service.PenaltyHistsCommandService;
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.domain.raffles.service.RafflesQueryService;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DrawsCommandServiceImpl implements DrawsCommandService {

    private final DrawsRepository drawsRepository;
    private final GoodsQueryService goodsQueryService;
    private final PenaltyHistsCommandService penaltyHistsCommandService;
    private final DrawsComposeService drawsComposeService;
    private final RafflesQueryService rafflesQueryService;
    private final HashQueryService hashQueryService;

    public DrawsResponse.EditWinnerStatus editWinnerStatus(Members members, Long winnerId, DrawsRequest.DrawsRequestDTO dto) {
        if (!(members.getRole().equals(Role.ADMIN))) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        Draws draws = drawsRepository.findById(winnerId).orElseThrow(() -> new ApiException(ErrorStatus._RAFFLES_NOT_COMPLETED));
        try {
            if (Status.valueOf(dto.getStatus()).equals(Status.CONFIRM) && !(draws.getStatus().equals(Status.WINNER))) {
                throw new ApiException(ErrorStatus._CONFIRM_REQUIRE_WINNER);
            }
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorStatus._BAD_REQUEST);
        }

        Status newStatus = Status.valueOf(dto.getStatus());
        draws.updateStatus(newStatus);
        switch (newStatus) {
            case CONFIRM:
                changeConfirmRaffleEnd(draws);
                break;

            case NO_SHOW:
                waitlistUpdate(draws);
                noshowPenalty(draws.getRaffleId().getMemberId());
                break;

            case CANCEL:
                waitlistUpdate(draws);
                break;

            default:
                throw new ApiException(ErrorStatus._BAD_REQUEST);
        }

        return DrawsResponse.EditWinnerStatus.builder()
                .id(draws.getId())
                .raffleId(draws.getRaffleId().getId())
                .status(draws.getStatus())
                .sequence(draws.getSequence())
                .build();
    }

    private void waitlistUpdate(Draws draws) {
        List<Draws> drawsList = drawsRepository.findAllByGoodsIdAndStatus(draws.getRaffleId().getGoodsId().getId(), Status.WAITLIST);
        if (!drawsList.isEmpty()) {
            drawsList.get(0).updateStatus(Status.WINNER);
            for (Draws value : drawsList) {
                value.decreaseSequence();
            }
            drawsRepository.saveAll(drawsList);
        }
    }

    private void noshowPenalty(Members members) {
        members.updatePenalty(members.getPenaltyCnt() + 5);
        penaltyHistsCommandService.updatePenaltyHists(members, "노쑈 패널티 부여", 5);
    }

    private void changeConfirmRaffleEnd(Draws draws) {
        List<Draws> drawsList = drawsRepository.findAllByGoodsIdAndStatus(draws.getRaffleId().getGoodsId().getId(), Status.WINNER);
        if (drawsList.isEmpty()) {
            drawsList = drawsRepository.findAllByGoodsIdAndStatus(draws.getRaffleId().getGoodsId().getId(), Status.WAITLIST);
            for (Draws waitDraw : drawsList) {
                drawsComposeService.nonWinnerPointRefund(waitDraw);
            }
        }
    }

    public DrawsResponse.DrawsResponseResultListDTO verificationSeed(Long goodsId, String hash) {
        Hash findHash = hashQueryService.findByHash(hash);
        double seed = findHash.getSeed();

        Goods goods = goodsQueryService.goodsFindById(goodsId);
        List<Raffles> rafflesList = rafflesQueryService.findAllByGoodsIdOrderByPointAsc(goods);

        List<Draws> drawsListResult = RaffleDraw.performDraw(seed, rafflesList, goods);
        List<DrawsResponse.DrawsResponseResultDTO> drawsResponseResultDTOList = drawsListResult.stream()
                .map(draws -> DrawsResponse.DrawsResponseResultDTO.builder()
                        .status(draws.getStatus())
                        .sequence(draws.getSequence())
                        .memberId(draws.getRaffleId().getMemberId().getId())
                        .memberName(draws.getRaffleId().getMemberId().getName())
                        .build())
                .toList();

        return DrawsResponse.DrawsResponseResultListDTO.builder()
                .drawsList(drawsResponseResultDTOList)
                .build();
    }

}
