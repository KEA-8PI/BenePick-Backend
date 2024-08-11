package com._pi.benepick.domain.wishlists.service;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.service.GoodsQueryService;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.wishlists.dto.WishlistResponse;
import com._pi.benepick.domain.wishlists.entity.Wishlists;
import com._pi.benepick.domain.wishlists.repository.WishlistsRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class WishlistsComposeServiceImpl implements WishlistsComposeService {
    private final WishlistsQueryService wishlistsQueryService;
    private final WishlistsRepository wishlistsRepository;
    private final GoodsQueryService goodsQueryService;

    @Override
    public WishlistResponse.WishlistSuccessDTO deleteWishlist(Long wishlistId, Members members) {
        Wishlists wishlists = wishlistsQueryService.getWishlistsById(wishlistId);
        if (!wishlists.getMemberId().getId().equals(members.getId())) {
            throw new ApiException(ErrorStatus._FORBIDDEN);
        }
        wishlistsRepository.deleteById(wishlistId);
        return WishlistResponse.WishlistSuccessDTO.builder()
                .id(wishlists.getId())
                .build();

    }

    @Override
    public WishlistResponse.WishlistAddDTO addWishlist(Members members, Long id) {
        Goods goods = goodsQueryService.getGoodsById(id);
        if( wishlistsQueryService.getWishlistsByMemberIdAndId(members, id)){
            Wishlists wishlists = Wishlists.builder()
                    .goodsId(goods)
                    .memberId(members)
                    .build();
            wishlistsRepository.save(wishlists);
            return WishlistResponse.WishlistAddDTO.from(wishlists);
        }else{
            throw new ApiException(ErrorStatus._WISHLIST_ALREADY_EXISTS);
        }

    }
}
