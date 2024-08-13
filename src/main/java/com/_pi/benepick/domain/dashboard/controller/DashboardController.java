package com._pi.benepick.domain.dashboard.controller;

import com._pi.benepick.domain.dashboard.dto.DashboardResponse;
import com._pi.benepick.domain.dashboard.service.DashboardComposeService;
import com._pi.benepick.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public ApiResponse<DashboardResponse.DashboardResponseDTO> getDashboard(@RequestParam(required = false) String category, @RequestParam @DateTimeFormat LocalDateTime startDate, @RequestParam @DateTimeFormat LocalDateTime endDate) {
        return ApiResponse.onSuccess(dashboardComposeService.getDashboard(category, startDate, endDate));
    }
}
