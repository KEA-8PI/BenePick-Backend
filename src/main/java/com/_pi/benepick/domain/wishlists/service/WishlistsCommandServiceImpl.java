package com._pi.benepick.domain.wishlists.service;


import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.wishlists.dto.WishlistResponse;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.repository.GoodsRepository;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.wishlists.dto.WishlistRequest;
import com._pi.benepick.domain.wishlists.dto.WishlistResponse.*;
import com._pi.benepick.domain.wishlists.entity.Wishlists;
import com._pi.benepick.domain.wishlists.repository.WishlistsRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WishlistsCommandServiceImpl implements WishlistsCommandSerivce{
    private final WishlistsRepository wishlistsRepository;
    private final GoodsRepository goodsRepository;
    
    @Override
    public WishlistResponse.WishlistSuccessDTO deleteWishlist(Long wishlistId, Members members){
        Wishlists wishlists = wishlistsRepository.findById(wishlistId).orElseThrow(()->new ApiException(ErrorStatus._WISHLIST_NOT_FOUND));
    if(!wishlists.getMemberId().getId().equals(members.getId())){
        throw new ApiException(ErrorStatus._FORBIDDEN);
    }
    wishlistsRepository.deleteById(wishlistId);
        return WishlistResponse.WishlistSuccessDTO.builder()
                .id(wishlists.getId())
                .build();

    }

    @Override
    public WishlistAddDTO addWishlist(Members members, Long id){
       Goods goods=goodsRepository.findById(id).orElseThrow(()->new ApiException(ErrorStatus._GOODS_NOT_FOUND));
        Wishlists wishlists= Wishlists.builder()
                .goodsId(goods)
                .memberId(members)
                .build();
       wishlistsRepository.save(wishlists);
        return WishlistAddDTO.from(wishlists);

    }
}
