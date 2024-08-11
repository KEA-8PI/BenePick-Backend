package com._pi.benepick.domain.wishlists.service;

import com._pi.benepick.domain.goods.entity.GoodsFilter;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.wishlists.dto.WishlistResponse;

public interface WishlistsQueryService {
    //위시리스트 응모 상태별 조회
    WishlistResponse.WishlistListDTO getWishList(GoodsStatus goodsStatus, Integer page, Integer size, GoodsFilter sortBy, Members members);
}
