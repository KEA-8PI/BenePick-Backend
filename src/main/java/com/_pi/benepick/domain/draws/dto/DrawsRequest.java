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
        //        Enum 에 알맞은 값이 들어오는지 유효성검사를 해야하는데 해당 부분은 validator 구현한 후 나중에 적용
        private String status; // 응모 포인트

        public static Draws updateStatus(Draws draws, DrawsRequestDTO requestDTO) {
            return Draws.builder()
                    .raffleId(draws.getRaffleId())
                    .status(Status.valueOf(requestDTO.status))
                    .id(draws.getId())
                    .sequence(draws.getSequence())
                    .build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class DrawsValidationDTO {
        private String seed;
        private Long goodsId;
    }

}
