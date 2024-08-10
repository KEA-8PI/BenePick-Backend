package com._pi.benepick.domain.wishlists.dto;
import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.goods.entity.Goods;

import com._pi.benepick.domain.wishlists.entity.Wishlists;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class WishlistResponse {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WishlistDTO{
        private Long id;
        private String name;
        private Long amounts;
        private String image;
        private String goodsStatus;
        private LocalDateTime raffleStartAt;
        private LocalDateTime raffleEndAt;
        private String category;
        private Long count;

        public static WishlistDTO from(Wishlists wishlists){
            return WishlistDTO.builder()
                    .id(wishlists.getId())
                    .name(wishlists.getGoodsId().getName())
                    .amounts(wishlists.getGoodsId().getAmounts())
                    .image(wishlists.getGoodsId().getImage())
                    .goodsStatus(wishlists.getGoodsId().getGoodsStatus().name())
                    .raffleEndAt(wishlists.getGoodsId().getRaffleEndAt())
                    .raffleStartAt(wishlists.getGoodsId().getRaffleStartAt())
                    .count((long)wishlists.getGoodsId().getRaffles().size())
                    .build();
        }
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WishlistListDTO{
        List<WishlistDTO> wishlistDTOS;
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WishlistSuccessDTO{
        Long id;
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WishlistAddDTO{
        Long id;
        GoodsResponse.GoodsResponseDTO goods;

        public static WishlistAddDTO from(Wishlists wishlists){
            return WishlistAddDTO.builder()
                    .id(wishlists.getId())
                    .goods(GoodsResponse.GoodsResponseDTO.from(wishlists.getGoodsId()))
                    .build();
        }

    }

}
