package com._pi.benepick.domain.raffles.dto;

import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.raffles.entity.Raffles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class RafflesResponse {

    private RafflesResponse() {
        throw new IllegalStateException("Utility Class.");
    }

    // 상품별 응모 조회 반환 값에 사용되는 format
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RafflesResponseByGoodsDTO {
        private String id; // 사원_id
        private String memberName; // 멤버_이름
        private Long point; // 사용포인트
        private LocalDateTime rafflesAt;

        public static RafflesResponseByGoodsDTO from(Raffles raffles) {
            return RafflesResponseByGoodsDTO.builder()
                    .id(raffles.getMemberId().getId())
                    .memberName(raffles.getMemberId().getName())
                    .point(raffles.getPoint())
                    .rafflesAt(raffles.getUpdatedAt())
                    .build();
        }

    }

    // 유저별 응모 조회 반환 값에 사용되는 format
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RafflesResponseByMembersDTO {
        private Long goodsId; // 응모_id
        private String goodsName;
        private Long point; // 사용포인트
        private LocalDateTime rafflesAt;
        private String categoryName; //카테고리 이름

        public static RafflesResponseByMembersDTO of(Raffles raffles, String categoryName) {
            return RafflesResponseByMembersDTO.builder()
                    .goodsId(raffles.getGoodsId().getId())
                    .goodsName(raffles.getGoodsId().getName())
                    .point(raffles.getPoint())
                    .rafflesAt(raffles.getUpdatedAt())
                    .categoryName(categoryName)
                    .build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplyRafflesResponseByGoodsId {
        private Long id; //응모_id
        private String memberId; //멤버_id
        private Long goodsId; //상품_id
        private Long point; //사용포인트
        private char penaltyFlag;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RafflesResponseByGoodsListDTO {
        private List<RafflesResponseByGoodsDTO> rafflesResponseByGoodsList;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RafflesResponseByMembersListDTO {
        private List<RafflesResponseByMembersDTO> rafflesResponseByMembersList;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CurrentStateByGoodsDTO {
        private int grade;
        private Long point;

    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CurrentStateByGoodsListDTO {
        private List<CurrentStateByGoodsDTO> currentStateByGoodsDTOList;
        private int average;
        private Long total;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RafflesAndGoodsCategory {
        private Raffles raffles;
        private String category;
    }
}