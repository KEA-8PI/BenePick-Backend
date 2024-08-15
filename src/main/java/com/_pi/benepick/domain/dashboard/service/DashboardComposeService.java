package com._pi.benepick.domain.dashboard.service;

import com._pi.benepick.domain.dashboard.dto.DashboardResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface DashboardComposeService {
    DashboardResponse.DashboardResponseDTO getDashboard(String category, LocalDateTime startDate, LocalDateTime endDate);

    DashboardResponse.WinnerPointsPerRafflesDto getWinnerPointsPerRaffles(String category, LocalDate startDate, LocalDate endDate);

    DashboardResponse.TotalPointsPerRafflesDto getTotalPointsPerRaffles(String category, LocalDate startDate, LocalDate endDate);

    DashboardResponse.RefillRatesPerRafflesDto getRefillRatesPerRaffles(String category, LocalDate startDate, LocalDate endDate);

    DashboardResponse.MostWonRanksDto getMostWonRanks(String category, LocalDate startDate, LocalDate endDate);

    DashboardResponse.AvgWinnerPointsDto getAvgWinnerPoints(String category, LocalDate startDate, LocalDate endDate);
}
