package com._pi.benepick.domain.goods.dto;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class GoodsRequest {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GoodsRequestDTO {
        private String name; //이름
        private Long amounts; //수량
        private String image; //상품 사진
        private String description; //설명
        private Long price; //정가
        private Long discountPrice; //할인가
        private LocalDateTime raffleStartAt; //응모 시작일
        private LocalDateTime raffleEndAt; //응모 종료일
        private String category; //카테고리

        public Goods toEntity(GoodsStatus status) {
            return Goods.builder()
                    .name(name)
                    .amounts(amounts)
                    .image(image)
                    .description(description)
                    .price(price)
                    .discountPrice(discountPrice)
                    .raffleStartAt(raffleStartAt)
                    .raffleEndAt(raffleEndAt)
                    .seeds("-1")
                    .goodsStatus(status)
                    .build();
        }
    }
}
