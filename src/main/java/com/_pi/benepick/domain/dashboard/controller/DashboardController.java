package com._pi.benepick.domain.dashboard.controller;

import com._pi.benepick.domain.dashboard.dto.DashboardResponse;
import com._pi.benepick.domain.dashboard.service.DashboardComposeService;
import com._pi.benepick.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Dashboard", description = "대시보드 API")
public class DashboardController {
    private final DashboardComposeService dashboardComposeService;

    @Operation(summary = "대시보드 조회", description = "대시보드를 조회합니다.")
    @GetMapping("/dashboard")
    public ApiResponse<DashboardResponse.DashboardResponseDTO> getDashboard(@RequestParam(required = false) @Valid String category, @RequestParam @DateTimeFormat @Valid LocalDateTime startDate, @RequestParam @DateTimeFormat @Valid LocalDateTime endDate) {
        return ApiResponse.onSuccess(dashboardComposeService.getDashboard(category, startDate, endDate));
    }

    @Operation(summary = "회차별 당첨자 평균 응모 포인트", description = "회차별 당첨자 평균 응모 포인트를 조회합니다.")
    @GetMapping("/dashboard/avgWinnerPoints-PerRaffles")
    public ApiResponse<DashboardResponse.WinnerPointsPerRafflesDto> getWinnerPointsPerRaffles(@RequestParam(required = false) @Valid String category,
                                                                                       @RequestParam @DateTimeFormat @Valid LocalDateTime startDate,
                                                                                       @RequestParam @DateTimeFormat @Valid LocalDateTime endDate) {
        return ApiResponse.onSuccess(dashboardComposeService.getWinnerPointsPerRaffles(category, startDate, endDate));
    }

    @Operation(summary = "회차별 총 응모 포인트", description = "회차별 총 응모 포인트를 조회합니다.")
    @GetMapping("/dashboard/totalPoints-PerRaffles")
    public ApiResponse<DashboardResponse.TotalPointsPerRafflesDto> getTotalPointsPerRaffles(@RequestParam(required = false) @Valid String category,
                                                                                  @RequestParam @DateTimeFormat @Valid LocalDateTime startDate,
                                                                                  @RequestParam @DateTimeFormat @Valid LocalDateTime endDate) {
        return ApiResponse.onSuccess(dashboardComposeService.getTotalPointsPerRaffles(category, startDate, endDate));
    }

    @Operation(summary = "회차별 대기 충원율", description = "회차별 대기 충원율을 조회합니다.")
    @GetMapping("/dashboard/refillRates-PerRaffles")
    public ApiResponse<DashboardResponse.RefillRatesPerRafflesDto> getRefillRates(@RequestParam(required = false) @Valid String category,
                                                                                  @RequestParam @DateTimeFormat @Valid LocalDateTime startDate,
                                                                                  @RequestParam @DateTimeFormat @Valid LocalDateTime endDate) {
        return ApiResponse.onSuccess(dashboardComposeService.getRefillRatesPerRaffles(category, startDate, endDate));
    }

    @Operation(summary = "응모자 중 당첨자 순위 top5", description = "응모자 중 당첨자 순위 top5를 조회합니다.")
    @GetMapping("/dashboard/mostWonRanks")
    public ApiResponse<DashboardResponse.MostWonRanksDto> getMostWonRanks(@RequestParam(required = false) @Valid String category,
                                                                            @RequestParam @DateTimeFormat @Valid LocalDateTime startDate,
                                                                            @RequestParam @DateTimeFormat @Valid LocalDateTime endDate) {
        return ApiResponse.onSuccess(dashboardComposeService.getMostWonRanks(category, startDate, endDate));
    }

    @Operation(summary = "당첨자 평균 응모 포인트", description = "당첨자 평균 응모 포인트를 조회합니다.")
    @GetMapping("/dashboard/avgWinnerPoints")
    public ApiResponse<DashboardResponse.AvgWinnerPointsDto> getAvgWinnerPoints(@RequestParam(required = false) @Valid String category,
                                                                                @RequestParam @DateTimeFormat @Valid LocalDateTime startDate,
                                                                                @RequestParam @DateTimeFormat @Valid LocalDateTime endDate) {
        return ApiResponse.onSuccess(dashboardComposeService.getAvgWinnerPoints(category, startDate, endDate));
    }
}
