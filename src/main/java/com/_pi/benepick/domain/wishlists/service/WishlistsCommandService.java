package com._pi.benepick.domain.wishlists.service;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.wishlists.dto.WishlistResponse;
import com._pi.benepick.domain.wishlists.entity.Wishlists;

public interface WishlistsCommandService {

    WishlistResponse.WishlistSuccessDTO deleteWishlist(Long wishlistId, Members members);

    Wishlists createWishlists(Goods goods, Members members);
}
