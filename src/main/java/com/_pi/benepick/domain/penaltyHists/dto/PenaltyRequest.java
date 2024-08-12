package com._pi.benepick.domain.penaltyHists.dto;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.penaltyHists.entity.PenaltyHists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PenaltyRequest {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChangePenaltyHistDTO {
        private Long penaltyCnt;
        private String content;
        private Members member;
        private Long totalPenalty;

        public PenaltyHists toEntity(ChangePenaltyHistDTO changePenaltyHistDTO,Long result){
            return PenaltyHists.builder()
                    .content(changePenaltyHistDTO.getContent())
                    .memberId(changePenaltyHistDTO.getMember())
                    .penaltyCount(changePenaltyHistDTO.getPenaltyCnt().intValue())
                    .totalPenalty(result.intValue())
                    .build();
        }

    }
}
