package com._pi.benepick.domain.goods.dto;

import com._pi.benepick.domain.goods.entity.Goods;
import java.util.List;

import com._pi.benepick.domain.members.entity.Members;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class GoodsResponse {

    // 상품 상세 조회
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class GoodsDetailResponseDTO{
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
        private Long count; //응모자 수

        public static GoodsDetailResponseDTO of(Goods goods, String category){
            return GoodsDetailResponseDTO.builder()
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
                    .count((long) goods.getRaffles().size())
                    .build();
        }
    }

    // 상품 목록 조회
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class GoodsResponseDTO{
        private Long id; //상품_id
        private String name; //이름
        private String goodsStatus; //상품응모상태
        private LocalDateTime raffleStartAt; //응모 시작일
        private LocalDateTime raffleEndAt; //응모 종료일

        public static GoodsResponseDTO from(Goods goods){
            return GoodsResponseDTO.builder()
                    .id(goods.getId())
                    .name(goods.getName())
                    .goodsStatus(String.valueOf(goods.getGoodsStatus()))
                    .raffleStartAt(goods.getRaffleStartAt())
                    .raffleEndAt(goods.getRaffleEndAt())
                    .build();
        }
    }
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class GoodsListResponseDTO {
        private List<GoodsResponseDTO> goodsDTOList;
        private int totalCnt;
    }


    // 시드 값 조회
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class GoodsSeedsResponseDTO {
        private String seeds; //시드값

        public static GoodsSeedsResponseDTO from(Goods goods){
            return GoodsSeedsResponseDTO.builder()
                    .seeds(goods.getHash().getHash())
                    .build();
        }
    }

    // 상품 검색
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class GoodsSearchResponseDTO{
        private Long id; //상품_id
        private String name; //이름
        private Long amounts; //수량
        private String image; //상품 사진
        private String goodsStatus; //상품응모상태
        private LocalDateTime raffleStartAt; //응모 시작일
        private LocalDateTime raffleEndAt; //응모 종료일
        private String category; //카테고리
        private Long count; //응모자 수
        private boolean isWishlist; //위시리스트 등록 여부

        public static GoodsSearchResponseDTO of(Goods goods, String category, Members member) {
            return GoodsSearchResponseDTO.builder()
                    .id(goods.getId())
                    .name(goods.getName())
                    .amounts(goods.getAmounts())
                    .image(goods.getImage())
                    .goodsStatus(goods.getGoodsStatus().name())
                    .raffleStartAt(goods.getRaffleStartAt())
                    .raffleEndAt(goods.getRaffleEndAt())
                    .category(category)
                    .count((long) goods.getRaffles().size())
                    .isWishlist(member != null && goods.isWishlistForMember(member.getId()))
                    .build();
        }
    }
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class GoodsListSearchResponseDTO {
        private List<GoodsSearchResponseDTO> goodsSearchDTOList;
        private int totalCnt;
    }

    // 상품 추가 및 수정
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class GoodsAddResponseDTO{
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

        public static GoodsAddResponseDTO of(Goods goods, String category){
            return GoodsAddResponseDTO.builder()
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
    // 상품 파일 업로드
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class GoodsUploadResponseDTO {
        private List<GoodsAddResponseDTO> goodsUploadDTOList;
    }

    // 상품 삭제
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class GoodsDeleteResponseDTO {
        private List<Long> deletedGoodsList;

        public static GoodsDeleteResponseDTO from(List<Long> goodsList){
            return GoodsDeleteResponseDTO.builder()
                    .deletedGoodsList(goodsList)
                    .build();
        }
    }
}

