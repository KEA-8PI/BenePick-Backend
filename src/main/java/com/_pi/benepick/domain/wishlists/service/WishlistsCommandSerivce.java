package com._pi.benepick.domain.wishlists.service;

import com._pi.benepick.domain.wishlists.dto.WishlistResponse;

public interface WishlistsCommandSerivce {
    WishlistResponse.WishlistSuccessDTO deleteWishlist(Long goodsId);
}
