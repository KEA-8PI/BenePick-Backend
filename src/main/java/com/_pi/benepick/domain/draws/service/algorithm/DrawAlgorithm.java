package com._pi.benepick.domain.draws.service.algorithm;

import com._pi.benepick.domain.raffles.entity.Raffles;
import lombok.Getter;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@Getter
public class DrawAlgorithm {

    private double seed;

    // 시드를 사용하여 초기화하는 생성자
    public DrawAlgorithm(double seed) {
        this.seed = seed;
        if (this.seed == -1) {
            this.seed = generateSeed();
        }
    }

    // Method to generate a seed value between 0 and 1
    public double generateSeed() {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextDouble();
    }

    // 총 포인트를 계산하는 메서드
    public Long total(List<Raffles> rafflesList) {
        long totalPoints = 0L;
        for (Raffles raffle : rafflesList) {
            totalPoints += raffle.getPoint();
        }
        return totalPoints;
    }

    // 각 사용자의 포인트 비율을 계산하는 메서드
    public double percent(long point, long totalPoints) {
        return (double) point / totalPoints;
    }

    // 랜덤 값을 생성하는 메서드
    public double random() {
        Random random = new Random();
        random.setSeed(Double.doubleToLongBits(seed));
        double randomValue = random.nextDouble();
        seed = randomValue; // 새로운 랜덤 값을 시드로 업데이트
        return randomValue;
    }

    // 추첨을 실행하는 메인 메서드
    public Raffles drawAlgorithm(List<Raffles> rafflesList) {
        long totalPoints = total(rafflesList);

        // 누적 비율 범위 계산
        double cumulativePercentage = 0.0;
        double randomValue = random();
        Raffles winner = null;

        for (Raffles raffle : rafflesList) {
            double percentage = percent(raffle.getPoint(), totalPoints);
            cumulativePercentage += percentage;

            if (randomValue <= cumulativePercentage) {
                winner = raffle;
                break;
            }
        }

        // 당첨자 출력 (필요에 따라 이 부분을 수정 가능)
        if (winner != null) {
            double percentage = percent(winner.getPoint(), totalPoints);
        } else {
        }

        return winner;
    }
}