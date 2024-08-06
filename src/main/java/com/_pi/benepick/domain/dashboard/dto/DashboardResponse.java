package com._pi.benepick.domain.dashboard.dto;

import lombok.*;
import java.util.List;
import java.util.Map;

public class DashboardResponse {
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class DashboardResponseDTO{
        private List<Double> avgWinnerPointsPerRaffles; //회차별 당첨자 평균 응모 포인트
        private List<Double> totalPointsPerRaffles; //회차별 총 응모 포인트
        private List<Double> refillRatesPerRaffles; //회차별 대기 충원율
        private List<Map.Entry<Integer, Double>> mostWinnedRanks; //응모자 중 당첨자 순위 top5
        private Double avgWinnerPoints; //당첨자 평균 응모 포인트
    }
}
