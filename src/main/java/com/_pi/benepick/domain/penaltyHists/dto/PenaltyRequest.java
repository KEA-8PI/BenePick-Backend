package com._pi.benepick.domain.penaltyHists.dto;

import com._pi.benepick.domain.members.entity.Members;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PenaltyRequest {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class changePenaltyHistDTO{
        private Long penaltyCnt;
        private String members;
        private String content;
        private Members member;
        private Long totalPenalty;

    }
}
