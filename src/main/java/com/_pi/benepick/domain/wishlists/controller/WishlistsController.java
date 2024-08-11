package com._pi.benepick.domain.wishlists.controller;

import com._pi.benepick.domain.goods.entity.GoodsFilter;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.domain.wishlists.dto.WishlistResponse.*;
import com._pi.benepick.domain.wishlists.service.WishlistsQueryService;

import com._pi.benepick.domain.wishlists.service.WishlistsCommandSerivce;
import com._pi.benepick.global.common.annotation.MemberObject;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.ApiResponse;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Wishlists", description = "위시리스트 API")
@RequestMapping("/wishlists")
public class WishlistsController {
    private final WishlistsQueryService wishlistsQueryService;
    private final MembersRepository membersRepository;
    private final WishlistsCommandSerivce wishlistsCommandSerivce;


    @Operation(summary = "위시리스트 응모 상태별 조회",description = "사용자가 본인의 위시리스트를 조회합니다.")
    @GetMapping("/{goodsStatus}")
    public ApiResponse<WishlistListDTO> getwishList(@Parameter(hidden = true) @MemberObject Members member, @PathVariable GoodsStatus goodsStatus, @RequestParam Integer page, @RequestParam Integer size, @RequestParam GoodsFilter sortBy){
        return ApiResponse.onSuccess(
             wishlistsQueryService.getWishList(goodsStatus,page,size,sortBy,member)
        );
    }

    @Operation(summary = "위시리스트 추가", description = "사용자가 위시리스트를 추가합니다.")
    @PostMapping("/add/{goodsId}")
    public ApiResponse<WishlistAddDTO> addwishList(@Parameter(hidden = true) @MemberObject Members member,@PathVariable Long goodsId){
        return ApiResponse.onSuccess(wishlistsCommandSerivce.addWishlist(member,goodsId));
    }

    @Operation(summary = "위시리스트 삭제",description = "사용자가 위시리스트를 삭제합니다." )
    @DeleteMapping("/delete/{wishlistId}")
    public ApiResponse<WishlistSuccessDTO> deletewishList(@Parameter(hidden = true) @MemberObject Members member,@PathVariable Long wishlistId){
        return ApiResponse.onSuccess(
               wishlistsCommandSerivce.deleteWishlist(wishlistId,member)
        );
    }

}
