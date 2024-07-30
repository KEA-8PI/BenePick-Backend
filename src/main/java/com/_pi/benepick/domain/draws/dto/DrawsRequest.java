package com._pi.benepick.domain.draws.dto;

import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.draws.entity.Status;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.raffles.entity.Raffles;
import lombok.*;

public class DrawsRequest {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class DrawsRequestDTO {
        private String status; // 응모 포인트

        public Draws toEntity(Draws draws) {
            return Draws.builder()
                    .raffleId(draws.getRaffleId())
                    .status(Status.valueOf(status))
                    .id(draws.getId())
                    .sequence(draws.getSequence())
                    .build();
        }
    }

}
