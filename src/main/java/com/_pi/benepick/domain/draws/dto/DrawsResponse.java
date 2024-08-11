package com._pi.benepick.domain.draws.dto;

import com._pi.benepick.domain.draws.entity.Status;
import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.raffles.entity.Raffles;
import jakarta.persistence.*;
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
        private String categoryName; //카테고리 이름

        public static DrawsResponse.DrawsResponseByMembersDTO of(Draws draws, String categoryName) {
            return DrawsResponseByMembersDTO.builder()
                    .id(draws.getRaffleId().getId())
                    .memberId(draws.getRaffleId().getMemberId().getId())
                    .goodsId(draws.getRaffleId().getGoodsId().getId())
                    .point(draws.getRaffleId().getPoint())
                    .sequence(draws.getSequence())
                    .drawStatus(draws.getStatus())
                    .rafflesAt(draws.getRaffleId().getUpdatedAt())
                    .categoryName(categoryName)
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
                    .rafflesAt(draws.getRaffleId().getUpdatedAt())
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
                    .rafflesAt(draws.getRaffleId().getUpdatedAt())
                    .sequence(draws.getSequence())
                    .build();
        }

    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EditWinnerStatus {
        private Long id; //추첨_id
        private Long raffleId; //응모_id
        private int sequence; // 순서
        private Status status; // WINNER, WAITLIST, CANCLE, NOSHOW
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DrawsResponseByWinnerGoodsIdDTO {
        private String memberId; // 멤버_id
        private String memberName; // 멤버_이름
        private Long point; // 사용포인트
        private Status status;
        private LocalDateTime rafflesAt;

        public static DrawsResponseByWinnerGoodsIdDTO from(Draws draw) {
            return DrawsResponseByWinnerGoodsIdDTO.builder()
                    .rafflesAt(draw.getUpdatedAt())
                    .point(draw.getRaffleId().getPoint())
                    .memberName(draw.getRaffleId().getMemberId().getName())
                    .memberId(draw.getRaffleId().getMemberId().getId())
                    .status(draw.getStatus())
                    .build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DrawsResponseByWinnerGoodsIdListDTO {
        private List<DrawsResponse.DrawsResponseByWinnerGoodsIdDTO> drawsResponseByWinnerGoodsIdDTOS;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DrawsResponseByWaitlistGoodsIdDTO {
        private String memberId; // 멤버_id
        private String memberName; // 멤버_이름
        private Long point; // 사용포인트
        private int status;
        private LocalDateTime rafflesAt;

        public static DrawsResponseByWaitlistGoodsIdDTO from(Draws draw) {
            return DrawsResponseByWaitlistGoodsIdDTO.builder()
                .rafflesAt(draw.getUpdatedAt())
                .point(draw.getRaffleId().getPoint())
                .memberName(draw.getRaffleId().getMemberId().getName())
                .memberId(draw.getRaffleId().getMemberId().getId())
                .status(draw.getSequence())
                .build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DrawsResponseByWaitlistGoodsIdListDTO {
        private List<DrawsResponse.DrawsResponseByWaitlistGoodsIdDTO> drawsResponseByWaitlistGoodsIdDTOS;
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
