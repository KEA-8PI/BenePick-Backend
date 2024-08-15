package com._pi.benepick.domain.raffles.dto;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.raffles.entity.Raffles;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class RafflesRequest {

    private RafflesRequest() {
        throw new IllegalStateException("Utility Class.");
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class RafflesRequestDTO {
        @NotNull(message = "Point is required")
        @Min(value = 1, message = "Point must be greater than or equal to 1")
        private Long point; // 응모 포인트

        // 생성
        public static Raffles toEntity(Members memberId, Goods goodsId, char penaltyFlag) {
            return Raffles.builder()
                    .memberId(memberId)
                    .goodsId(goodsId)
                    .point(0L)
                    .penaltyFlag(penaltyFlag)
                    .build();
        }

    }

}
