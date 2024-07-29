package com._pi.benepick.domain.raffles.dto;

import com._pi.benepick.domain.raffles.entity.Raffles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class RafflesResponse {

    // 상품별 응모 조회 반환 값에 사용되는 format
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RafflesResponseByGoodsDTO {
        private Long id; // 응모_id
        private String memberId; // 멤버_id
        private String memberName; // 멤버_이름
        private Long goodsId; // 상품_id
        private Long point; // 사용포인트
        private LocalDateTime rafflesAt;

        public static RafflesResponseByGoodsDTO from(Raffles raffles) {
            return RafflesResponseByGoodsDTO.builder()
                    .id(raffles.getId())
                    .memberId(raffles.getMemberId().getId())
                    .memberName(raffles.getMemberId().getName())
                    .goodsId(raffles.getGoodsId().getId())
                    .point(raffles.getPoint())
                    .rafflesAt(raffles.getUpdated_at())
                    .build();
        }

    }

    // 유저별 응모 조회 반환 값에 사용되는 format
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RafflesResponseByMembersDTO {
        private Long id; // 응모_id
        private String memberId; // 멤버_id
        private Long goodsId; // 상품_id
        private Long point; // 사용포인트
        private LocalDateTime rafflesAt;
        private String category_name; //카테고리 이름

        public static RafflesResponseByMembersDTO of(Raffles raffles, String categoryName) {
            return RafflesResponseByMembersDTO.builder()
                    .id(raffles.getId())
                    .memberId(raffles.getMemberId().getId())
                    .goodsId(raffles.getGoodsId().getId())
                    .point(raffles.getPoint())
                    .rafflesAt(raffles.getUpdated_at())
                    .category_name(categoryName)
                    .build();
        }
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
}