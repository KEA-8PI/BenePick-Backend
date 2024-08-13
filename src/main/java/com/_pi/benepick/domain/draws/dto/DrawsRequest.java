package com._pi.benepick.domain.draws.dto;

import com._pi.benepick.domain.draws.entity.Status;
import jakarta.validation.constraints.NotNull;
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
        @NotNull(message = "Status is required")
        private Status status;
    }

}