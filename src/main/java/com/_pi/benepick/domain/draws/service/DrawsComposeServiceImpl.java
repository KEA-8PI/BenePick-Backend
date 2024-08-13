package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.alarm.domain.MessageType;
import com._pi.benepick.domain.alarm.service.AlarmService;
import com._pi.benepick.domain.draws.dto.DrawsRequest;
import com._pi.benepick.domain.draws.dto.DrawsResponse;
import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.draws.entity.Status;
import com._pi.benepick.domain.draws.service.algorithm.DrawAlgorithm;
import com._pi.benepick.domain.draws.service.algorithm.RaffleDraw;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goods.service.GoodsQueryService;
import com._pi.benepick.domain.goodsCategories.service.GoodsCategoriesQueryService;
import com._pi.benepick.domain.hash.entity.Hash;
import com._pi.benepick.domain.hash.service.HashCommandService;
import com._pi.benepick.domain.hash.service.HashQueryService;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.domain.penaltyHists.dto.PenaltyRequest;
import com._pi.benepick.domain.penaltyHists.service.PenaltyHistsCommandService;
import com._pi.benepick.domain.pointHists.dto.PointHistsRequest;
import com._pi.benepick.domain.pointHists.service.PointHistsCommandService;
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.domain.raffles.service.RafflesQueryService;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DrawsComposeServiceImpl implements DrawsComposeService {

    private final DrawsQueryService drawsQueryService;
    private final DrawsCommandService drawsCommandService;
    private final GoodsQueryService goodsQueryService;
    private final RafflesQueryService rafflesQueryService;
    private final HashCommandService hashCommandService;
    private final PointHistsCommandService pointHistsCommandService;
    private final PenaltyHistsCommandService penaltyHistsCommandService;
    private final HashQueryService hashQueryService;
    private final GoodsCategoriesQueryService goodsCategoriesQueryService;
    private final AlarmService alarmService;

    public DrawsResponse.DrawsResponseByMembersListDTO getCompleteRafflesByMemberId(Members member) {
        if (!(member.getRole().equals(Role.MEMBER))) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        List<DrawsResponse.DrawsResponseByMembersDTO> drawsResponseByMembersDTOS = (drawsQueryService.findByMemberId(member)).stream()
                .filter(draws -> draws.getRaffleId().getGoodsId().getGoodsStatus() == GoodsStatus.COMPLETED)
                .map(draws -> {
                    String categoryName = goodsCategoriesQueryService.getGoodsCategory(draws.getRaffleId());

                    return DrawsResponse.DrawsResponseByMembersDTO.of(draws, categoryName);
                })
                .toList();

        return DrawsResponse.DrawsResponseByMembersListDTO.builder()
                .drawsResponseByMembersList(drawsResponseByMembersDTOS)
                .build();
    }

    public void drawStart(LocalDateTime now) {
        List<Goods> goodsList = goodsQueryService.findByRaffleEndAtBeforeAndGoodsStatus(now);

        for (Goods goods : goodsList) {
            // 현재 시각이 응모종료시간보다 이후여야하고, 상태가 PROGRESS 여야 한다.
            if (!(LocalDateTime.now().isAfter(goods.getRaffleEndAt()) && goods.getGoodsStatus().equals(GoodsStatus.PROGRESS)))
                throw new ApiException(ErrorStatus._BAD_REQUEST);
            List<Raffles> rafflesList = rafflesQueryService.findAllByGoodsIdOrderByPointAsc(goods);

            double seed = DrawAlgorithm.generateSeed();
            Hash savedHash = hashCommandService.saveHash(seed);
            goods.startDraw(savedHash, GoodsStatus.COMPLETED);

            List<Draws> drawsList = RaffleDraw.performDraw(seed, rafflesList, goods);

            for (Draws draws : drawsList) {
                if (draws.getStatus().equals(Status.NON_WINNER)) {
                    nonWinnerPointRefund(draws);
                } else if (draws.getStatus().equals(Status.WINNER)) {
                    sendAlarm(draws, MessageType.CONGRATULATIONS);
                }
            }
            drawsCommandService.saveDrawsList(drawsList);
        }

    }

    private void sendAlarm(Draws draws, MessageType type) {
        Members members = draws.getRaffleId().getMemberId();
        String url = "http://localhost:3000/goods/" + draws.getRaffleId().getGoodsId();
        String contents = alarmService.getMessageFactory(members, url, type);
        alarmService.saveMessage(members.getId(), members.getName(), contents);
    }

    public DrawsResponse.EditWinnerStatus editWinnerStatus(Members members, Long winnerId, DrawsRequest.DrawsRequestDTO dto) {
        if (!(members.getRole().equals(Role.ADMIN))) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        Draws draws = drawsQueryService.findDrawsById(winnerId);
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

            case WINNER:
                sendAlarm(draws, MessageType.ADDITIONAL);
                break;

            default:
                throw new ApiException(ErrorStatus._BAD_REQUEST);
        }

        return DrawsResponse.EditWinnerStatus.from(draws);
    }

    private void waitlistUpdate(Draws draws) {
        List<Draws> drawsList = drawsQueryService.findAllByGoodsIdAndStatus(draws.getRaffleId().getGoodsId(), Status.WAITLIST);
        if (!drawsList.isEmpty()) {
            drawsList.get(0).updateStatus(Status.WINNER);
            for (Draws value : drawsList) {
                value.decreaseSequence();
            }
            drawsCommandService.saveDrawsList(drawsList);
        }
    }

    private void noshowPenalty(Members members) {
        members.updatePenalty(members.getPenaltyCnt() + 5);
        penaltyHistsCommandService.createPenaltyHists(new PenaltyRequest.ChangePenaltyHistDTO(
                5L,"노쑈 패널티 부여",members,members.getPenaltyCnt()
        ));
    }

    private void changeConfirmRaffleEnd(Draws draws) {
        List<Draws> drawsList = drawsQueryService.findAllByGoodsIdAndStatus(draws.getRaffleId().getGoodsId(), Status.WINNER);
        if (drawsList.isEmpty()) {
            drawsList = drawsQueryService.findAllByGoodsIdAndStatus(draws.getRaffleId().getGoodsId(), Status.WAITLIST);
            for (Draws waitDraw : drawsList) {
                nonWinnerPointRefund(waitDraw);
            }
        }
    }

    private void nonWinnerPointRefund(Draws waitDraw) {
        Members waitMembers = waitDraw.getRaffleId().getMemberId();
        waitMembers.increasePoint(Math.round(waitDraw.getRaffleId().getPoint() / 2.0));
        pointHistsCommandService.refundPointHists(new PointHistsRequest.RefundPointHistDTO(
                "낙첨 포인트 반환", waitDraw
        ));
    }

    public DrawsResponse.DrawsResponseResultListDTO verificationSeed(Long goodsId, String hash) {
        Hash findHash = hashQueryService.findByCryptoHash(hash);
        double seed = findHash.getSeed();

        Goods goods = goodsQueryService.getGoodsById(goodsId);
        List<Raffles> rafflesList = rafflesQueryService.findAllByGoodsIdOrderByPointAsc(goods);

        List<Draws> drawsListResult = RaffleDraw.performDraw(seed, rafflesList, goods);
        List<DrawsResponse.DrawsResponseResultDTO> drawsResponseResultDTOList = drawsListResult.stream()
                .map(DrawsResponse.DrawsResponseResultDTO::from)
                .toList();

        return DrawsResponse.DrawsResponseResultListDTO.builder()
                .drawsList(drawsResponseResultDTOList)
                .build();
    }

}
