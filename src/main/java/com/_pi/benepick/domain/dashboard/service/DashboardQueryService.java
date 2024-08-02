package com._pi.benepick.domain.dashboard.service;

import com._pi.benepick.domain.dashboard.dto.DashboardResponse;

import java.time.LocalDateTime;

public interface DashboardQueryService {
    DashboardResponse.DashboardResponseDTO getDashboard(String category, LocalDateTime startDate, LocalDateTime endDate);
}
