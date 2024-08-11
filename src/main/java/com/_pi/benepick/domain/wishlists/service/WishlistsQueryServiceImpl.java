package com._pi.benepick.domain.wishlists.service;

import com._pi.benepick.domain.goods.entity.GoodsFilter;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.wishlists.dto.WishlistResponse;
import com._pi.benepick.domain.wishlists.entity.Wishlists;
import com._pi.benepick.domain.wishlists.repository.WishlistsRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishlistsQueryServiceImpl implements WishlistsQueryService{

    private final WishlistsRepository wishlistsRepository;


    //위시리스트 응모 상태별 조회
    @Override
    public WishlistResponse.WishlistListDTO getWishList(GoodsStatus goodsStatus, Integer page, Integer size, GoodsFilter sortBy, Members members){
        PageRequest pageRequest=createPageRequest(page, size, sortBy);

        String memberId = members.getId();

        Page<Wishlists> wishlistsPage;
        if (GoodsFilter.POPULAR.equals(sortBy)) {
            wishlistsPage = wishlistsRepository.searchWishlistsByRaffleCount(memberId, goodsStatus, pageRequest);

        } else {
            wishlistsPage = wishlistsRepository.findAllByMemberId_IdAndGoodsId_GoodsStatus(memberId, goodsStatus, pageRequest);
        }
        List<WishlistResponse.WishlistDTO> wishlistDTOS = (wishlistsPage != null ? wishlistsPage.getContent() : Collections.emptyList()).stream()
                .map(w -> WishlistResponse.WishlistDTO.from((Wishlists) w))
                .collect(Collectors.toList());

        return WishlistResponse.WishlistListDTO.builder()
                .wishlistDTOS(wishlistDTOS)
                .build();
    }


    private PageRequest createPageRequest(Integer page, Integer size, GoodsFilter sortBy) {
        Sort sort;
        switch (sortBy) {
            case NEWEST: // 최신순
                sort = Sort.by(Sort.Order.desc("goodsId.createdAt"), Sort.Order.asc("id"));
                break;
            case END: // 종료임박순
                sort = Sort.by(Sort.Order.asc("goodsId.raffleEndAt"), Sort.Order.asc("id"));
                break;
            default:
                sort = Sort.by(Sort.Order.desc("id")); // 기본
        }
        return PageRequest.of(page, size, sort);
    }

    @Override
    public Wishlists getWishlistsById(Long id){
        return wishlistsRepository.findById(id).orElseThrow(()->new ApiException(ErrorStatus._WISHLIST_NOT_FOUND));
    }

    @Override
    public Wishlists getWishlistsByMemberIdAndId(Members members, Long id){
        return wishlistsRepository.findByIdAndMemberId(id,members).orElseThrow(()->new ApiException(ErrorStatus._WISHLIST_ALREADY_EXISTS));
    }
}
