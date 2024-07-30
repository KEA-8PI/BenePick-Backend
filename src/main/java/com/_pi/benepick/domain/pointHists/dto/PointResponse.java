package com._pi.benepick.domain.pointHists.dto;

import com._pi.benepick.domain.members.entity.Members;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class PointResponse {


    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PointHistDTO{

        private Long pointChange;
        private String content;
        private LocalDateTime createdAt;
        private Long totalPoint;

    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PointHistListDTO{
        private List<PointHistDTO> pointHistDTOS;
    }

}
