package com._pi.benepick.domain.wishlists.dto;

import lombok.*;

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
        Long id;
    }
}
