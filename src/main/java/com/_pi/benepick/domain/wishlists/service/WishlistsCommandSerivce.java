package com._pi.benepick.domain.wishlists.service;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.wishlists.dto.WishlistResponse;

public interface WishlistsCommandSerivce {

    WishlistResponse.WishlistSuccessDTO deleteWishlist(Long wishlistId, Members members);
    WishlistResponse.WishlistAddDTO addWishlist(Members members, Long id);
}
