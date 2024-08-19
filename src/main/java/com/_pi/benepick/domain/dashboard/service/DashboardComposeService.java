package com._pi.benepick.domain.dashboard.service;

import com._pi.benepick.domain.dashboard.dto.DashboardResponse;

import java.time.LocalDateTime;

public interface DashboardComposeService {
    DashboardResponse.DashboardResponseDTO getDashboard(String category, LocalDateTime startDate, LocalDateTime endDate);

    DashboardResponse.WinnerPointsPerRafflesDto getWinnerPointsPerRaffles(String category, LocalDateTime startDate, LocalDateTime endDate);

    DashboardResponse.TotalPointsPerRafflesDto getTotalPointsPerRaffles(String category, LocalDateTime startDate, LocalDateTime endDate);

    DashboardResponse.RefillRatesPerRafflesDto getRefillRatesPerRaffles(String category, LocalDateTime startDate, LocalDateTime endDate);

    DashboardResponse.MostWonRanksDto getMostWonRanks(String category, LocalDateTime startDate, LocalDateTime endDate);

    DashboardResponse.AvgWinnerPointsDto getAvgWinnerPoints(String category, LocalDateTime startDate, LocalDateTime endDate);
}
