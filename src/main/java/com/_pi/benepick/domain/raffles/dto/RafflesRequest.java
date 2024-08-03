package com._pi.benepick.domain.raffles.dto;

import com._pi.benepick.domain.draws.dto.DrawsRequest;
import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.draws.entity.Status;
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

        public static Raffles toEntity(Members memberId, Goods goodsId, RafflesRequestDTO dto) {
            return Raffles.builder()
                    .memberId(memberId)
                    .goodsId(goodsId)
                    .point(dto.point)
                    .build();
        }

        public static Raffles toEntity(Long id, Members memberId, Goods goodsId, RafflesRequestDTO dto) {
            return Raffles.builder()
                    .id(id)
                    .memberId(memberId)
                    .goodsId(goodsId)
                    .point(dto.point)
                    .build();
        }

        public static RafflesRequestDTO updatePoint(RafflesRequestDTO dto, Long point) {
            return RafflesRequestDTO.builder()
                    .point(point)
                    .build();
        }
    }

}
