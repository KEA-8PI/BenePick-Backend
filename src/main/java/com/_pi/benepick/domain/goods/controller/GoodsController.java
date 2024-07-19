package com._pi.benepick.domain.goods.controller;

import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.goods.service.GoodsCommandService;
import com._pi.benepick.domain.goods.service.GoodsQueryService;
import com._pi.benepick.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/goods")
@Tag(name = "Goods", description = "상품 API")
public class GoodsController {

    private final GoodsQueryService goodsQueryService;
    private final GoodsCommandService goodsCommandService;

    //상품 엑셀 파일로 추가

    //상품 추가

    //상품 상세 조회 + 썸네일 조회
    @Operation(summary = "상품 상세 조회", description = "상품의 상세 정보를 조회합니다.")
    @GetMapping("/view/{goodsId}")
    public ApiResponse<GoodsResponse.GoodsResponseDTO> getGoodsInfo(@PathVariable Long goodsId) {
        return ApiResponse.onSuccess(goodsQueryService.getGoodsInfo(goodsId));
    }

    //상품 목록 조회

    //시드 값 조회

    //상품 삭제

}
