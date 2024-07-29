package com._pi.benepick.domain.raffles.dto;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.raffles.entity.Raffles;
import lombok.*;

public class RafflesRequest {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class RafflesRequestDTO {
        private Long point; // 응모 포인트

        public Raffles toEntity(Members memberId, Goods goodsId) {
            return Raffles.builder()
                    .memberId(memberId)
                    .goodsId(goodsId)
                    .point(point)
                    .build();
        }
    }

}
