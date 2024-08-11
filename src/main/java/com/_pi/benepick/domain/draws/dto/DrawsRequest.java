package com._pi.benepick.domain.draws.dto;

import lombok.*;

public class DrawsRequest {

    private DrawsRequest() {
        throw new IllegalStateException("Utility Class.");
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class DrawsRequestDTO {
        // Enum 에 알맞은 값이 들어오는지 유효성 검사를 해야 하는데, 해당 부분은 validator 구현한 후 나중에 적용
        private String status; // 응모 포인트
    }

}