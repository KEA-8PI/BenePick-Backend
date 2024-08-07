package com._pi.benepick.domain.draws.service.algorithm;

import com._pi.benepick.domain.draws.dto.DrawsStart;
import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.draws.entity.Status;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.raffles.entity.Raffles;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RaffleDraw {

    public static List<Draws> performDraw(double seed, List<Raffles> rafflesList, Goods goods) {
        DrawAlgorithm drawAlgorithm = new DrawAlgorithm(seed);
        List<Draws> drawsList = new ArrayList<>();

        List<DrawsStart.DrawsStartDTO> drawsStartDTOS = new ArrayList<>(rafflesList.stream()
                .map(raffles -> {
                    if (raffles.getPenaltyFlag() == 'T') {
                        return DrawsStart.DrawsStartDTO.builder()
                                .raffles(raffles)
                                .point(Math.round(raffles.getPoint() * 0.9))
                                .build();
                    } else {
                        return DrawsStart.DrawsStartDTO.builder()
                                .raffles(raffles)
                                .point(raffles.getPoint())
                                .build();
                    }
                }).toList());

        // 당첨자 추첨.
        for (int i = 0; i < goods.getAmounts(); i++) {
            if (drawsStartDTOS.isEmpty()) break;
            DrawsStart.DrawsStartDTO winner = drawAlgorithm.drawAlgorithm(drawsStartDTOS);
            drawsList.add(Draws.builder()
                    .raffleId(winner.getRaffles())
                    .sequence(0)
                    .status(Status.WINNER)
                    .build());

            drawsStartDTOS.remove(winner);
        }

        // 대기자 추첨.
        for (int i = 0; i < goods.getAmounts() * 2; i++) {
            if (drawsStartDTOS.isEmpty()) break;
            DrawsStart.DrawsStartDTO winner = drawAlgorithm.drawAlgorithm(drawsStartDTOS);
            drawsList.add(Draws.builder()
                    .raffleId(winner.getRaffles())
                    .sequence(i + 1)
                    .status(Status.WAITLIST)
                    .build());

            drawsStartDTOS.remove(winner);
        }

        for (DrawsStart.DrawsStartDTO drawsStartDTO : drawsStartDTOS) {
            drawsList.add(Draws.builder()
                    .raffleId(drawsStartDTO.getRaffles())
                    .sequence(0)
                    .status(Status.NON_WINNER)
                    .build());
        }

        return drawsList;
    }

}
