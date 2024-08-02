package com._pi.benepick.domain.dashboard.service;

import com._pi.benepick.domain.categories.entity.Categories;
import com._pi.benepick.domain.categories.repository.CategoriesRepository;
import com._pi.benepick.domain.dashboard.dto.DashboardResponse;
import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.draws.entity.Status;
import com._pi.benepick.domain.draws.repository.DrawsRepository;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.repository.GoodsRepository;
import com._pi.benepick.domain.goodsCategories.entity.GoodsCategories;
import com._pi.benepick.domain.goodsCategories.repository.GoodsCategoriesRepository;
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.domain.raffles.repository.RafflesRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardQueryServiceImpl implements DashboardQueryService {

    private final GoodsRepository goodsRepository;
    private final DrawsRepository drawsRepository;
    private final RafflesRepository rafflesRepository;
    private final CategoriesRepository categoriesRepository;
    private final GoodsCategoriesRepository goodsCategoriesRepository;

    @Override
    public DashboardResponse.DashboardResponseDTO getDashboard(String categoryName, LocalDateTime startDate, LocalDateTime endDate) {
        List<Goods> goodsList;

        // 카테고리
        if (categoryName != null && !categoryName.isEmpty()) {
            Categories category = categoriesRepository.findByName(categoryName).orElseThrow(() -> new ApiException(ErrorStatus._CATEGORY_NOT_FOUND));
            goodsList = goodsRepository.findGoodsByCategoryId(category.getId());
        } else {
            goodsList = goodsRepository.findAll();
        }

        // 기간 내 raffleEndAt이 있도록 필터링 및 응모종료순 정렬
        goodsList = goodsList.stream()
                .filter(goods -> goods.getRaffleEndAt().isAfter(startDate) && goods.getRaffleEndAt().isBefore(endDate))
                .sorted(Comparator.comparing(Goods::getRaffleEndAt))
                .collect(Collectors.toList());

        // 회차별 당첨자 평균 응모 포인트
        List<Double> avgWinnerPointsPerRaffles = calculateAvgWinnerPointsPerRaffles(goodsList);
        // 회차별 총 응모 포인트
        List<Double> totalPointsPerRaffles = calculateTotalPointsPerRaffles(goodsList);
        // 회차별 대기 충원율
        List<Double> refillRatesPerRaffles = calculateRefillRatesPerRaffles(goodsList);
        return DashboardResponse.DashboardResponseDTO.builder()
                .avgWinnerPointsPerRaffles(avgWinnerPointsPerRaffles)
                .totalPointsPerRaffles(totalPointsPerRaffles)
                .refillRatesPerRaffles(refillRatesPerRaffles)
                .build();
    }

    // 회차별 당첨자 평균 응모 포인트
    private List<Double> calculateAvgWinnerPointsPerRaffles(List<Goods> goodsList) {
        return goodsList.stream()
                .map(goods -> {
                    List<Draws> draws = drawsRepository.findDrawsByGoodsIdAndStatus(goods.getId(), Status.WINNER);
                    List<Long> points = draws.stream()
                            .map(draw -> draw.getRaffleId().getPoint())
                            .collect(Collectors.toList());
                    return points.stream()
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
                    List<Long> points = raffles.stream()
                            .map(Raffles::getPoint)
                            .collect(Collectors.toList());
                    return points.stream()
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
                    List<Draws> draws = drawsRepository.findByRaffleIds(raffleIds);
                    long nonWinnerCount = drawsRepository.countByRaffleIdsAndStatus(raffleIds, Status.NON_WINNER);
                    long cancelOrNoShowCount = drawsRepository.countByRaffleIdsAndStatus(raffleIds, Status.CANCEL) + drawsRepository.countByRaffleIdsAndStatus(raffleIds, Status.NO_SHOW);
                    long totalDraws = draws.size();
                    double refillRate = (totalDraws == 0) ? 0.0 : (cancelOrNoShowCount / (double) (totalDraws - nonWinnerCount));
                    return refillRate;
                })
                .collect(Collectors.toList());
    }

}