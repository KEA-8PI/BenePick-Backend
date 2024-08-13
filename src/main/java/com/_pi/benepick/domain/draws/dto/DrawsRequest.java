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
        private String status;
    }

}