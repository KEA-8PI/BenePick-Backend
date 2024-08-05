package com._pi.benepick.domain.wishlists.controller;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.domain.wishlists.dto.WishlistResponse.*;
import com._pi.benepick.domain.wishlists.service.WishlistsCommandSerivce;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.ApiResponse;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import io.swagger.v3.oas.annotations.Operation;
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

    private final MembersRepository membersRepository;
    private final WishlistsCommandSerivce wishlistsCommandSerivce;
    @Operation(summary = "위시리스트 응모 상태별 조회 - Mockup API",description = "사용자가 본인의 위시리스트를 조회합니다.")
    @GetMapping("/{goodsStatus}")
    public ApiResponse<WishlistListDTO> getwishList(@PathVariable String goodsStatus){
        WishlistDTO wishlistDTO1=new WishlistDTO((long)1,"맥북",(long)1,"123","응모중","2023-02-01 00:34:13.778134","2023-02-01 00:34:13.778134","전자기기",(long)100);
        WishlistDTO wishlistDTO2=new WishlistDTO((long)1,"맥북",(long)1,"123","응모중","2023-02-01 00:34:13.778134","2023-02-01 00:34:13.778134","전자기기",(long)100);
        List<WishlistDTO> wishlistDTOS= Arrays.asList(wishlistDTO1,wishlistDTO2);

        return ApiResponse.onSuccess(
                WishlistListDTO.builder()
                        .wishlistDTOS(wishlistDTOS)
                        .build()
        );
    }

    @Operation(summary = "위시리스트 추가", description = "사용자가 위시리스트를 추가합니다.")
    @PostMapping("/add/{goodsId}")
    public ApiResponse<WishlistAddDTO> addwishList(@PathVariable Long goodsId){
        Members members=membersRepository.findById("string").orElseThrow(()->new ApiException(ErrorStatus._MEMBERS_NOT_FOUND));
        return ApiResponse.onSuccess(wishlistsCommandSerivce.addWishlist(members,goodsId));
    }

    @Operation(summary = "위시리스트 삭제 - MockupAPI",description = "사용자가 위시리스트를 삭제합니다." )
    @DeleteMapping("/delete/{wishlistsId}")
    public ApiResponse<WishlistSuccessDTO> deletewishList(@PathVariable String wishlistsId){
        return ApiResponse.onSuccess(
                WishlistSuccessDTO.builder()
                        .msg("삭제되었습니다.")
                        .build()
        );
    }

}
