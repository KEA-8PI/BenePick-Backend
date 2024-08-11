package com._pi.benepick.domain.pointHists.dto;

import com._pi.benepick.domain.members.entity.Members;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PointHistsRequest {
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class changePointHistDTO{
        private Long point;
        private String content;
        private Long totalPoint;
        private Members members;
    }
}
