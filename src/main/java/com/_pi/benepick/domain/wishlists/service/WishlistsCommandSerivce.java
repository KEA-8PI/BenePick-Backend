package com._pi.benepick.domain.wishlists.service;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.wishlists.dto.WishlistRequest;
import com._pi.benepick.domain.wishlists.dto.WishlistResponse;

public interface WishlistsCommandSerivce {
    WishlistResponse.WishlistAddDTO addWishlist(Members members, Long id);
}
