package com._pi.benepick.domain.draws.dto;

import com._pi.benepick.domain.raffles.entity.Raffles;
import lombok.*;
import java.util.List;

public class DrawsStart {

    private DrawsStart() {
        throw new IllegalStateException("Utility Class.");
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class DrawsStartDTO {
        private Raffles raffles;
        private Long point; // 사용포인트
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class DrawsStartListDTO {
        private List<DrawsStartDTO> drawsStartDTOList;
    }

}
