package com._pi.benepick.domain.penaltyHists.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class PenaltyResponse {


    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PenaltyResponseDTO{
        private LocalDateTime createdAt;
        private String content;
        private int penaltyCount;
        private int totalPenalty;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PenaltyListResponseDTO{
        private List<PenaltyResponseDTO> penaltyResponseDTOList;
    }
}
