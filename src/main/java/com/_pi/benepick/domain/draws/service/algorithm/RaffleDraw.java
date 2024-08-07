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
        List<Draws> drawsList = new ArrayList<>();
        for (Raffles raffles : rafflesList) {
            if (raffles.getPenaltyFlag() == 'T') {
                raffles.updatePenaltyPoint(Math.round(raffles.getPoint() * 0.9));
            }
        }

        // 당첨자 추첨.
        for (int i = 0; i < goods.getAmounts(); i++) {
            Raffles winner = drawAlgorithm.drawAlgorithm(rafflesList);
            drawsList.add(Draws.builder()
                    .raffleId(winner)
                    .sequence(0)
                    .status(Status.WINNER)
                    .build());

            if (winner != null) {
                rafflesList.remove(winner);
                if (rafflesList.isEmpty()) break;
            }
        }

        // 대기자 추첨.
        for (int i = 0; i < goods.getAmounts() * 2; i++) {
            Raffles winner = drawAlgorithm.drawAlgorithm(rafflesList);
            drawsList.add(Draws.builder()
                    .raffleId(winner)
                    .sequence(i + 1)
                    .status(Status.WAITLIST)
                    .build());

            if (winner != null) {
                rafflesList.remove(winner);
                if (rafflesList.isEmpty()) break;
            }
        }

        for (Raffles raffles : rafflesList) {
            drawsList.add(Draws.builder()
                    .raffleId(raffles)
                    .sequence(0)
                    .status(Status.NON_WINNER)
                    .build());
        }

        return drawsList;
    }

}
