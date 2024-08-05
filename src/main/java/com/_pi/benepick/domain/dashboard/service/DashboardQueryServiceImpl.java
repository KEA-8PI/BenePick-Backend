package com._pi.benepick.domain.dashboard.service;

import com._pi.benepick.domain.categories.entity.Categories;
import com._pi.benepick.domain.categories.repository.CategoriesRepository;
import com._pi.benepick.domain.dashboard.dto.DashboardResponse;
import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.draws.entity.Status;
import com._pi.benepick.domain.draws.repository.DrawsRepository;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.repository.GoodsRepository;
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.domain.raffles.repository.RafflesRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardQueryServiceImpl implements DashboardQueryService {

    private final GoodsRepository goodsRepository;
    private final DrawsRepository drawsRepository;
    private final RafflesRepository rafflesRepository;
    private final CategoriesRepository categoriesRepository;

    // 당첨자 status
    private static final List<Status> WinnedStatus = List.of(Status.WINNER, Status.CANCEL, Status.NO_SHOW, Status.CONFIRM);

    @Override
    public DashboardResponse.DashboardResponseDTO getDashboard(String categoryName, LocalDateTime startDate, LocalDateTime endDate) {
        List<Goods> goodsList = getGoodsList(categoryName, startDate, endDate);

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

    // 카테고리 + 날짜 범위 + 응모종료순 필터링
    private List<Goods> getGoodsList(String categoryName, LocalDateTime startDate, LocalDateTime endDate) {
        List<Goods> goodsList;
        if (categoryName != null && !categoryName.isEmpty()) {
            Categories category = categoriesRepository.findByName(categoryName)
                    .orElseThrow(() -> new ApiException(ErrorStatus._CATEGORY_NOT_FOUND));
            goodsList = goodsRepository.findGoodsByCategoryId(category.getId());
        } else {
            goodsList = goodsRepository.findAll();
        }
        return goodsList.stream()
                .filter(goods -> goods.getRaffleEndAt().isAfter(startDate) && goods.getRaffleEndAt().isBefore(endDate))
                .sorted(Comparator.comparing(Goods::getRaffleEndAt))
                .collect(Collectors.toList());
    }

    // 회차별 당첨자 평균 응모 포인트
    private List<Double> calculateAvgWinnerPointsPerRaffles(List<Goods> goodsList) {
        return goodsList.stream()
                .map(goods -> {
                    List<Draws> draws = drawsRepository.findDrawsByGoodsIdAndStatuses(goods.getId(), WinnedStatus);
                    return draws.stream()
                            .map(draw -> draw.getRaffleId().getPoint())
                            .mapToDouble(Long::doubleValue)
                            .average()
                            .orElse(0.0);
                })
                .collect(Collectors.toList());
    }

    // 회차별 총 응모 포인트
    private List<Double> calculateTotalPointsPerRaffles(List<Goods> goodsList) {
        return goodsList.stream()
                .map(goods -> {
                    List<Raffles> raffles = rafflesRepository.findAllByGoodsId(goods);
                    return raffles.stream()
                            .map(Raffles::getPoint)
                            .mapToDouble(Long::doubleValue)
                            .sum();
                })
                .collect(Collectors.toList());
    }

    // 회차별 대기 충원율
    private List<Double> calculateRefillRatesPerRaffles(List<Goods> goodsList) {
        return goodsList.stream()
                .map(goods -> {
                    List<Long> raffleIds = rafflesRepository.findRaffleIdsByGoodsId(goods.getId());
                    long totalDraws = drawsRepository.countByRaffleIdsAndStatuses(raffleIds, List.of(Status.WINNER, Status.WAITLIST, Status.CANCEL, Status.NO_SHOW, Status.CONFIRM));
                    long refillCount = drawsRepository.countByRaffleIdsAndStatuses(raffleIds, List.of(Status.CANCEL, Status.NO_SHOW));
                    return (totalDraws == 0) ? 0.0 : (refillCount / (double) totalDraws);
                })
                .collect(Collectors.toList());
    }

    // 응모자 중 당첨자 순위 top5
    private List<Map.Entry<Integer, Double>> calculateMostWinnedRanks(List<Goods> goodsList) {
        Map<Integer, Integer> rankCountMap = new HashMap<>();
        goodsList.forEach(goods -> {
            List<Raffles> raffles = rafflesRepository.findAllByGoodsId(goods);
            raffles.forEach(raffle -> {
                List<Draws> draws = drawsRepository.findDrawsByRaffleIdAndStatuses(raffle.getId(), WinnedStatus);
                for (int i = 0; i < draws.size(); i++) {
                    int rank = i + 1;
                    rankCountMap.put(rank, rankCountMap.getOrDefault(rank, 0) + 1);
                }
            });
        });
        int totalRankCount = rankCountMap.values().stream().mapToInt(Integer::intValue).sum();
        return rankCountMap.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(5)
                .map(entry -> Map.entry(entry.getKey(), entry.getValue() / (double) totalRankCount))
                .collect(Collectors.toList());
    }


    // 당첨자 평균 응모 포인트
    private Double calculateAvgWinnerPoints(List<Goods> goodsList) {
        List<Long> points = goodsList.stream()
                .flatMap(goods -> drawsRepository.findDrawsByGoodsIdAndStatuses(goods.getId(), WinnedStatus).stream())
                .map(draw -> draw.getRaffleId().getPoint())
                .toList();
        return points.stream()
                .mapToDouble(Long::doubleValue)
                .average()
                .orElse(0.0);
    }
}
