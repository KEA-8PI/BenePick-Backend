package com._pi.benepick.domain.goods.dto;

import com._pi.benepick.domain.goods.entity.Goods;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class GoodsResponse {

    // 상품 정보 조회
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
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

        public static GoodsResponseDTO of(Goods goods, String category){
            return GoodsResponseDTO.builder()
                    .id(goods.getId())
                    .name(goods.getName())
                    .amounts(goods.getAmounts())
                    .image(goods.getImage())
                    .description(goods.getDescription())
                    .goodsStatus(goods.getGoodsStatus().name())
                    .price(goods.getPrice())
                    .discountPrice(goods.getDiscountPrice())
                    .raffleStartAt(goods.getRaffleStartAt())
                    .raffleEndAt(goods.getRaffleEndAt())
                    .category(category)
                    .build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class GoodsListResponseDTO {
        private List<GoodsResponseDTO> goodsDTOList;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class GoodsSeedsResponseDTO {
        private Long seeds; //시드값

        public static GoodsSeedsResponseDTO from(Goods goods){
            return GoodsSeedsResponseDTO.builder()
                    .seeds(goods.getSeeds())
                    .build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class GoodsDeleteResponseDTO {
        private List<Long> deletedGoodsList;

        public static GoodsDeleteResponseDTO from(List<Goods> goodsList){
            return GoodsDeleteResponseDTO.builder()
                    .deletedGoodsList(goodsList.stream().map(Goods::getId).toList())
                    .build();
        }
    }
}
