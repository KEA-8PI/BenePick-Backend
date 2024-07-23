package com._pi.benepick.domain.goods.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class GoodsResponse {

    // 상품 정보 조회
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GoodsResponseDTO{
        private Long id; //상품_id
        private String name; //이름
        private Long amounts; //수량
        private String image; //상품 사진
        private String description; //설명
        private String goodsStatus; //상품응모상태
        private Long price; //정가
        private Long discountPrice; //할인가
        private LocalDateTime raffleStartAt; //응모 시작일
        private LocalDateTime raffleEndAt; //응모 종료일
        private String category; //카테고리
    }

}
