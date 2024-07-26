package com._pi.benepick.domain.raffles.dto;

import com._pi.benepick.domain.winners.entity.Status;
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
        private int sequence;
        private Status winnerStatus; // 당첨 상태
        private LocalDateTime rafflesAt;
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
        private int sequence;
        private Status winnerStatus; // 당첨 상태
        private LocalDateTime rafflesAt;
        private String category_name; //카테고리 이름
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