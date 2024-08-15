package com._pi.benepick.domain.dashboard.service;

import com._pi.benepick.domain.dashboard.dto.DashboardResponse;
import com._pi.benepick.domain.draws.entity.Status;
import com._pi.benepick.domain.draws.service.DrawsQueryService;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.service.GoodsComposeService;
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.domain.raffles.service.RafflesQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardComposeServiceImpl implements DashboardComposeService {
    private final GoodsComposeService goodsComposeService;
    private final DrawsQueryService drawsQueryService;
    private final RafflesQueryService rafflesQueryService;

    // 당첨자 status
    private static final List<Status> wonStatus = List.of(Status.WINNER, Status.CANCEL, Status.NO_SHOW, Status.CONFIRM);

    @Override
    public DashboardResponse.DashboardResponseDTO getDashboard(String categoryName, LocalDateTime startDate, LocalDateTime endDate) {
        List<Goods> goodsList = goodsComposeService.getGoodsList(categoryName, startDate.toLocalDate(), endDate.toLocalDate());

        // 회차별 당첨자 평균 응모 포인트
        List<Double> avgWinnerPointsPerRaffles = calculateAvgWinnerPointsPerRaffles(goodsList);
        // 회차별 총 응모 포인트
        List<Double> totalPointsPerRaffles = calculateTotalPointsPerRaffles(goodsList);
        // 회차별 대기 충원율
        List<Double> refillRatesPerRaffles = calculateRefillRatesPerRaffles(goodsList);
        // 응모자 중 당첨자 순위 top5
        List<Map.Entry<Integer, Double>> mostWinnedRanks = calculateMostWinnedRanks(goodsList);
        // 당첨자 평균 응모 포인트
        Double avgWinnerPoints = calculateAvgWinnerPoints(goodsList);

        return DashboardResponse.DashboardResponseDTO.builder()
                .avgWinnerPointsPerRaffles(avgWinnerPointsPerRaffles)
                .totalPointsPerRaffles(totalPointsPerRaffles)
                .refillRatesPerRaffles(refillRatesPerRaffles)
                .mostWinnedRanks(mostWinnedRanks)
                .avgWinnerPoints(avgWinnerPoints)
                .build();
    }

    @Cacheable(value = "winnerPointsPerRaffles", key = "'winnerPointsPerRaffles:' + #category + #startDate + #endDate")
    @Override
    public DashboardResponse.WinnerPointsPerRafflesDto getWinnerPointsPerRaffles(String category, LocalDate startDate, LocalDate endDate) {
        List<Goods> goods = goodsComposeService.getGoodsList(category, startDate, endDate);
        List<Double> avgWinnerPointsPerRaffles = calculateAvgWinnerPointsPerRaffles(goods);

        return DashboardResponse.WinnerPointsPerRafflesDto.builder()
                .avgWinnerPointsPerRaffles(avgWinnerPointsPerRaffles)
                .build();
    }

    // 회차별 당첨자 평균 응모 포인트
    private List<Double> calculateAvgWinnerPointsPerRaffles(List<Goods> goodsList) {
        return goodsList.stream()
                .map(goods -> drawsQueryService.getAveragePointByGoodsIdAndStatuses(goods.getId(), wonStatus))
                .map(avg -> avg != null ? avg : 0.0)
                .toList();
    }

    @Cacheable(value = "totalPointsPerRaffles", key = "'totalPointsPerRaffles:' + #category + #startDate + #endDate")
    @Override
    public DashboardResponse.TotalPointsPerRafflesDto getTotalPointsPerRaffles(String category, LocalDate startDate, LocalDate endDate) {
        List<Goods> goods = goodsComposeService.getGoodsList(category, startDate, endDate);
        List<Double> totalPointsPerRaffles = calculateTotalPointsPerRaffles(goods);

        return DashboardResponse.TotalPointsPerRafflesDto.builder()
                .totalPointsPerRaffles(totalPointsPerRaffles)
                .build();
    }

    // 회차별 총 응모 포인트
    private List<Double> calculateTotalPointsPerRaffles(List<Goods> goodsList) {
        return goodsList.stream()
                .map(goods -> {
                    List<Raffles> raffles = rafflesQueryService.findAllByGoodsId(goods);
                    return raffles.stream()
                            .map(Raffles::getPoint)
                            .mapToDouble(Long::doubleValue)
                            .sum();
                })
                .toList();
    }

    @Cacheable(value = "refillRatesPerRaffles", key = "'refillRatesPerRaffles:' + #category + #startDate + #endDate")
    @Override
    public DashboardResponse.RefillRatesPerRafflesDto getRefillRatesPerRaffles(String category, LocalDate startDate, LocalDate endDate) {
        List<Goods> goods = goodsComposeService.getGoodsList(category, startDate, endDate);
        List<Double> refillRatesPerRaffles = calculateRefillRatesPerRaffles(goods);

        return DashboardResponse.RefillRatesPerRafflesDto.builder()
                .refillRatesPerRaffles(refillRatesPerRaffles)
                .build();
    }

    // 회차별 대기 충원율
    private List<Double> calculateRefillRatesPerRaffles(List<Goods> goodsList) {
        return goodsList.stream()
                .map(goods -> {
                    List<Raffles> raffles = rafflesQueryService.findAllByGoodsId(goods);
                    List<Long> raffleIds = raffles.stream().map(Raffles::getId).toList();
                    long totalDraws = drawsQueryService.countByRaffleIdsAndStatuses(raffleIds, List.of(Status.WINNER, Status.WAITLIST, Status.CANCEL, Status.NO_SHOW, Status.CONFIRM));
                    long refillCount = drawsQueryService.countByRaffleIdsAndStatuses(raffleIds, List.of(Status.CANCEL, Status.NO_SHOW));
                    return (totalDraws == 0) ? 0.0 : (refillCount / (double) totalDraws);
                })
                .toList();
    }

    @Cacheable(value = "mostWonRanks", key = "'mostWonRanks:' + #category + #startDate + #endDate")
    @Override
    public DashboardResponse.MostWonRanksDto getMostWonRanks(String category, LocalDate startDate, LocalDate endDate) {
        List<Goods> goods = goodsComposeService.getGoodsList(category, startDate, endDate);
        List<Map.Entry<Integer, Double>> mostWonRanks = calculateMostWinnedRanks(goods);

        return DashboardResponse.MostWonRanksDto.builder()
                .mostWonRanks(mostWonRanks)
                .build();
    }

    // 응모자 중 당첨자 순위 top5
    private List<Map.Entry<Integer, Double>> calculateMostWinnedRanks(List<Goods> goodsList) {
        Map<Integer, Integer> rankCountMap = new HashMap<>();
        goodsList.forEach(goods -> {
            List<Raffles> sortedRaffles = rafflesQueryService.findAllByGoodsIdOrderByPointDesc(goods);
            for (int i = 0; i < sortedRaffles.size(); i++) {
                Raffles raffle = sortedRaffles.get(i);
                if(raffle.getDraw()!=null && wonStatus.contains(raffle.getDraw().getStatus())){
                    int rank = i + 1; // 1부터 시작하는 순위
                    rankCountMap.put(rank, rankCountMap.getOrDefault(rank, 0) + 1);
                }
            }
        });
        int totalRankCount = rankCountMap.values().stream().mapToInt(Integer::intValue).sum();
        return rankCountMap.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(5)
                .map(entry -> Map.entry(entry.getKey(), entry.getValue() / (double) totalRankCount))
                .toList();
    }

    @Cacheable(value = "avgWinnerPoints", key = "'avgWinnerPoints:' + #category + #startDate + #endDate")
    @Override
    public DashboardResponse.AvgWinnerPointsDto getAvgWinnerPoints(String category, LocalDate startDate, LocalDate endDate) {
        List<Goods> goods = goodsComposeService.getGoodsList(category, startDate, endDate);
        Double avgWinnerPoints = calculateAvgWinnerPoints(goods);

        return DashboardResponse.AvgWinnerPointsDto.builder()
                .avgWinnerPoints(avgWinnerPoints)
                .build();
    }

    // 당첨자 평균 응모 포인트
    private Double calculateAvgWinnerPoints(List<Goods> goodsList) {
        List<Long> points = goodsList.stream()
                .flatMap(goods -> drawsQueryService.getDrawsByGoodsIdAndStatuses(goods.getId(), wonStatus).stream())
                .map(draw -> draw.getRaffleId().getPoint())
                .toList();
        return points.stream()
                .mapToDouble(Long::doubleValue)
                .average()
                .orElse(0.0);
    }
}
