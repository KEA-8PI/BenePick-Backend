package com._pi.benepick.domain.wishlists.controller;

import com._pi.benepick.domain.wishlists.dto.WishlistResponse.*;
import com._pi.benepick.global.common.response.ApiResponse;
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

    @Operation(summary = "위시리스트 응모 상태별 조회 - Mockup API",description = "사용자가 본인의 위시리스트를 조회합니다.")
    @GetMapping("/{goods-status}")
    public ApiResponse<WishlistListDTO> getwishList(@PathVariable String status){
        WishlistDTO wishlistDTO1=new WishlistDTO((long)1,"맥북","123","응모중","2023-02-01 00:34:13.778134","2023-02-01 00:34:13.778134","전자기기",(long)100);
        WishlistDTO wishlistDTO2=new WishlistDTO((long)1,"맥북","123","응모중","2023-02-01 00:34:13.778134","2023-02-01 00:34:13.778134","전자기기",(long)100);
        List<WishlistDTO> wishlistDTOS= Arrays.asList(wishlistDTO1,wishlistDTO2);

        return ApiResponse.onSuccess(
                WishlistListDTO.builder()
                        .wishlistDTOS(wishlistDTOS)
                        .build()
        );
    }

    @Operation(summary = "위시리스트 추가 - MockupAPI", description = "사용자가 위시리스트를 추가합니다.")
    @PostMapping("/add/{goods_id}")
    public ApiResponse<WishlistSuccessDTO> addwishList(@PathVariable String goods_id){
        return ApiResponse.onSuccess(
                WishlistSuccessDTO.builder()
                        .msg("추가되었습니다")
                        .build()
        );
    }

    @Operation(summary = "위시리스트 삭제 - MockupAPI",description = "사용자가 위시리스트를 삭제합니다." )
    @DeleteMapping("/delete/{wishlists_id")
    public ApiResponse<WishlistSuccessDTO> deletewishList(@PathVariable String wishlists_id){
        return ApiResponse.onSuccess(
                WishlistSuccessDTO.builder()
                        .msg("삭제되었습니다.")
                        .build()
        );
    }

}
