package com._pi.benepick.domain.wishlists.service;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.service.GoodsQueryService;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.wishlists.dto.WishlistResponse;
import com._pi.benepick.domain.wishlists.entity.Wishlists;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WishlistsComposeServiceImpl implements WishlistsComposeService {
    private final WishlistsQueryService wishlistsQueryService;
   private final WishlistsCommandService wishlistsCommandService;
    private final GoodsQueryService goodsQueryService;


    @Override
    public WishlistResponse.WishlistAddDTO addWishlist(Members members, Long id) {
        Goods goods = goodsQueryService.getGoodsById(id);
        if( wishlistsQueryService.isWishlistsEmpty(members, id)){
            Wishlists wishlists=wishlistsCommandService.saveWishlists(goods,members);
            return WishlistResponse.WishlistAddDTO.from(wishlists);
        }else{
            throw new ApiException(ErrorStatus._WISHLIST_ALREADY_EXISTS);
        }

    }
}
