package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.draws.entity.Status;
import com._pi.benepick.domain.draws.repository.DrawsRepository;
import com._pi.benepick.domain.draws.service.algorithm.DrawAlgorithm;
import com._pi.benepick.domain.draws.service.algorithm.RaffleDraw;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goods.service.GoodsQueryService;
import com._pi.benepick.domain.hash.entity.Hash;
import com._pi.benepick.domain.hash.service.HashCommandService;
import com._pi.benepick.domain.members.entity.Members;
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

    private final DrawsRepository drawsRepository;
    private final GoodsQueryService goodsQueryService;
    private final RafflesQueryService rafflesQueryService;
    private final HashCommandService hashCommandService;
    private final PointHistsCommandService pointHistsCommandService;

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
                }
            }
            drawsRepository.saveAll(drawsList);
        }

    }

    public void nonWinnerPointRefund(Draws waitDraw) {
        Members waitMembers = waitDraw.getRaffleId().getMemberId();
        waitMembers.increasePoint(Math.round(waitDraw.getRaffleId().getPoint() / 2.0));
        pointHistsCommandService.refundPointHists(waitMembers, waitDraw, "낙첨 포인트 반환");
    }

}
