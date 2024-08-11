package com._pi.benepick.domain.draws.service.algorithm;

import com._pi.benepick.domain.draws.dto.DrawsStart;
import lombok.Getter;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@Getter
public class DrawAlgorithm {

    private double seed;
    private final Random random = new Random();

    public DrawAlgorithm(double seed) {
        this.seed = seed;
        if (this.seed == -1) {
            this.seed = generateSeed();
        }
    }

    public static double generateSeed() {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextDouble();
    }

    private Long total(List<DrawsStart.DrawsStartDTO> rafflesList) {
        long totalPoints = 0L;
        for (DrawsStart.DrawsStartDTO raffle : rafflesList) {
            totalPoints += raffle.getPoint();
        }
        return totalPoints;
    }

    private double percent(long point, long totalPoints) {
        return (double) point / totalPoints;
    }

    private double random() {
        random.setSeed(Double.doubleToLongBits(seed));
        double randomValue = random.nextDouble();
        seed = randomValue; // 새로운 랜덤 값을 시드로 업데이트
        return randomValue;
    }

    public DrawsStart.DrawsStartDTO drawAlgorithm(List<DrawsStart.DrawsStartDTO> rafflesList) {
        long totalPoints = total(rafflesList);
        double cumulativePercentage = 0.0;
        double randomValue = random();
        DrawsStart.DrawsStartDTO winner = null;

        for (DrawsStart.DrawsStartDTO raffle : rafflesList) {
            double percentage = percent(raffle.getPoint(), totalPoints);
            cumulativePercentage += percentage;

            if (randomValue <= cumulativePercentage) {
                winner = raffle;
                break;
            }
        }

        return winner;
    }
}