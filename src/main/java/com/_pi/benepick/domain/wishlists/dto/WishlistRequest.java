package com._pi.benepick.domain.wishlists.dto;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.wishlists.entity.Wishlists;
import lombok.*;

public class WishlistRequest {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WishlistRequestDTO{
        Long id;
    }
}
