package com._pi.benepick.domain.wishlists.dto;
import lombok.*;

public class WishlistRequest {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WishlistRequestDTO{
        Long id;
    }
}
