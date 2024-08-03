package com._pi.benepick.domain.draws.service.algorithm;

import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.draws.entity.Status;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.raffles.entity.Raffles;

import java.util.ArrayList;
import java.util.List;

public class RaffleDraw {

    public static List<Draws> performDraw(double seed, List<Raffles> rafflesList, Goods goods) {
        DrawAlgorithm drawAlgorithm = new DrawAlgorithm(seed);
        Raffles winner = drawAlgorithm.drawAlgorithm(rafflesList);
        List<Draws> drawsList = new ArrayList<>();
        drawsList.add(Draws.builder()
                .raffleId(winner)
                .sequence(0)
                .status(Status.WINNER)
                .build());

        // 당첨자 추첨.
        for (int i = 0; i < goods.getAmounts() - 1; i++) {
            if (winner != null) {
                rafflesList.remove(winner);
                if (rafflesList.isEmpty()) break;
            }
            // draws 테이블에 추가.
            winner = drawAlgorithm.drawAlgorithm(rafflesList);
            drawsList.add(Draws.builder()
                    .raffleId(winner)
                    .sequence(0)
                    .status(Status.WINNER)
                    .build());
        }

        // 대기자 추첨.
        for (int i = 0; i < goods.getAmounts() * 2; i++) {
            if (winner != null) {
                rafflesList.remove(winner);
                if (rafflesList.isEmpty()) break;
            }
            winner = drawAlgorithm.drawAlgorithm(rafflesList);
            drawsList.add(Draws.builder()
                    .raffleId(winner)
                    .sequence(i + 1)
                    .status(Status.WAITLIST)
                    .build());
        }

        return drawsList;
    }

}
