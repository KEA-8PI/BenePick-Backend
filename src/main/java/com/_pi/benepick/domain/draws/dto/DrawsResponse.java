package com._pi.benepick.domain.draws.dto;

import com._pi.benepick.domain.draws.entity.Status;
import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.raffles.entity.Raffles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class DrawsResponse {

    // 유저별 응모 조회 반환 값에 사용되는 format
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DrawsResponseByMembersDTO {
        private Long id; // 응모_id
        private String memberId; // 멤버_id
        private Long goodsId; // 상품_id
        private Long point; // 사용포인트
        private int sequence;
        private Status drawStatus; // 당첨 상태
        private LocalDateTime rafflesAt;
        private String category_name; //카테고리 이름

        public static DrawsResponse.DrawsResponseByMembersDTO of(Draws draws, String categoryName) {
            return DrawsResponseByMembersDTO.builder()
                    .id(draws.getRaffleId().getId())
                    .memberId(draws.getRaffleId().getMemberId().getId())
                    .goodsId(draws.getRaffleId().getGoodsId().getId())
                    .point(draws.getRaffleId().getPoint())
                    .sequence(draws.getSequence())
                    .drawStatus(draws.getStatus())
                    .rafflesAt(draws.getRaffleId().getUpdated_at())
                    .category_name(categoryName)
                    .build();
        }

        public static DrawsResponse.DrawsResponseByMembersDTO from(Draws draws) {
            return DrawsResponseByMembersDTO.builder()
                    .id(draws.getRaffleId().getId())
                    .memberId(draws.getRaffleId().getMemberId().getId())
                    .goodsId(draws.getRaffleId().getGoodsId().getId())
                    .point(draws.getRaffleId().getPoint())
                    .sequence(draws.getSequence())
                    .drawStatus(draws.getStatus())
                    .rafflesAt(draws.getRaffleId().getUpdated_at())
                    .build();
        }
    }

    // 상품별 응모 조회 반환 값에 사용되는 format
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DrawsResponseByGoodsDTO {
        private Long id; // 응모_id
        private String memberId; // 멤버_id
        private String memberName; // 멤버_이름
        private Long goodsId; // 상품_id
        private Long point; // 사용포인트
        private int sequence;
        private Status drawStatus; // 당첨 상태
        private LocalDateTime rafflesAt;

        public static DrawsResponse.DrawsResponseByGoodsDTO from(Draws draws) {
            return DrawsResponseByGoodsDTO.builder()
                    .id(draws.getRaffleId().getId())
                    .memberId(draws.getRaffleId().getMemberId().getId())
                    .memberName(draws.getRaffleId().getMemberId().getName())
                    .goodsId(draws.getRaffleId().getGoodsId().getId())
                    .point(draws.getRaffleId().getPoint())
                    .drawStatus(draws.getStatus())
                    .rafflesAt(draws.getRaffleId().getUpdated_at())
                    .build();
        }

    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DrawsResponseResultDTO {
        private Status status;
        private int sequence;
        private String memberId;
        private String memberName;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DrawsResponseByMembersListDTO {
        private List<DrawsResponse.DrawsResponseByMembersDTO> drawsResponseByMembersList;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DrawsResponseByGoodsListDTO {
        private List<DrawsResponse.DrawsResponseByGoodsDTO> drawsResponseByGoodsDTOList;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DrawsResponseResultListDTO {
        private List<DrawsResponse.DrawsResponseResultDTO> drawsList;
    }

}
