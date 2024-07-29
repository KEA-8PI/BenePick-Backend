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
        private String createdAt;
        private String content;
        private Long penaltyCount;
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
