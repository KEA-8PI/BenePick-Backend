package com._pi.benepick.domain.wishlists.service;


import com._pi.benepick.domain.wishlists.dto.WishlistResponse;
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
    @Override
    public WishlistResponse.WishlistSuccessDTO deleteWishlist(Long wishlistId){
        Wishlists wishlists = wishlistsRepository.findById(wishlistId).orElseThrow(()->new ApiException(ErrorStatus._WISHLIST_NOT_FOUND));

    wishlistsRepository.deleteById(wishlistId);
        return WishlistResponse.WishlistSuccessDTO.builder()
                .id(wishlists.getId())
                .msg("위시리스트 삭제완료")
                .build();

    }


}
