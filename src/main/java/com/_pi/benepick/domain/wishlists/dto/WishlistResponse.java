package com._pi.benepick.domain.wishlists.dto;

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
        private String raffleStartAt;
        private String raffleEndAt;
        private String category;
        private Long count;
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
        String msg;
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WishlistAddDTO{
        Long id;
        Goods goods;

        public static WishlistAddDTO from(Wishlists wishlists){
            return WishlistAddDTO.builder()
                    .id(wishlists.getId())
                    .goods(wishlists.getGoodsId())
                    .build();
        }

    }

}
