package com._pi.benepick.domain.dashboard.controller;

import com._pi.benepick.domain.dashboard.dto.DashboardResponse;
import com._pi.benepick.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Dashboard", description = "대시보드 API")
public class DashboardController {
    @Operation(summary = "대시보드 조회 - Mockup API", description = "대시보드를 조회합니다.")
    @GetMapping("/dashboard")
    public ApiResponse<DashboardResponse.DashboardResponseDTO> getGoodsList(@RequestParam String category, @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        Double result = 1.0;
        Double result1 = 2.0;
        List<Double> resultList = Arrays.asList(result, result1);
        int result2 = 1;
        int result3 = 2;
        List<Integer> resultList2 = Arrays.asList(result2, result3);
        return ApiResponse.onSuccess(DashboardResponse.DashboardResponseDTO.builder()
                .avgWinnerPointsPerRaffles(resultList)
                .totalPointsPerRaffles(resultList)
                .refillRatesPerRaffles(resultList)
                .mostWinnedRanks(resultList2)
                .avgWinnerPoints(100.5)
                .build());    }
}
