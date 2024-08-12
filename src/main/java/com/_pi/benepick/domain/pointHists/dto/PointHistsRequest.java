package com._pi.benepick.domain.pointHists.dto;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.pointHists.entity.PointHists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PointHistsRequest {
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChangePointHistDTO {
        private Long point;
        private String content;
        private Long totalPoint;
        private Members members;

        public PointHists toEntity(ChangePointHistDTO changePointHistDTO, Long result){
            return PointHists.builder()
                    .pointChange(changePointHistDTO.getPoint())
                    .content(changePointHistDTO.getContent())
                    .totalPoint(result)
                    .memberId(changePointHistDTO.getMembers())
                    .build();
        }
    }
}
