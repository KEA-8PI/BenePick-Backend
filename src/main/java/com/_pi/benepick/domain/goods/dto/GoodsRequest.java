package com._pi.benepick.domain.goods.dto;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class GoodsRequest {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class GoodsRequestDTO {
        @NotNull(message = "이름은 필수 입력값입니다.")
        @Size(max = 50, message = "이름은 50자를 넘길 수 없습니다.")
        private String name; //이름

        @NotNull(message = "수량은 필수 입력값입니다.")
        @Size(min = 1, message = "수량은 1과 같거나 커야합니다.")
        private Long amounts; //수량
        private String image; //상품 사진

        @Size(max = 255, message = "설명은 255자를 넘길 수 없습니다.")
        private String description; //설명

        @NotNull(message = "정가는 필수 입력값입니다.")
        @Size(min = 1, message = "정가는 1과 같거나 커야합니다.")
        private Long price; //정가

        @NotNull(message = "할인가는 필수 입력값입니다.")
        private Long discountPrice; //할인가

        @NotNull(message = "응모 시작일은 필수 입력값입니다.")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime raffleStartAt; //응모 시작일

        @NotNull(message = "응모 종료일은 필수 입력값입니다.")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime raffleEndAt; //응모 종료일

        @NotNull(message = "카테고리는 필수 입력값입니다.")
        private String category; //카테고리

        public void restrictName() {
            if (name.length() > 50) {
                name = name.substring(0, 50);
            }
        }

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
                    .goodsStatus(status)
                    .build();
        }
    }

}
