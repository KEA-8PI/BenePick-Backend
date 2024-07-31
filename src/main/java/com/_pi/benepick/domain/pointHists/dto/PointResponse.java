package com._pi.benepick.domain.pointHists.dto;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.pointHists.entity.PointHists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.awt.*;
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

        public static PointHistDTO from(PointHists pointHists){
            return PointHistDTO.builder()
                    .content(pointHists.getContent())
                    .totalPoint(pointHists.getTotalPoint())
                    .pointChange(pointHists.getPointChange())
                    .createdAt(pointHists.getCreated_at())
                    .build();
        }

    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PointHistListDTO{
        private List<PointHistDTO> pointHistDTOS;
    }

}
