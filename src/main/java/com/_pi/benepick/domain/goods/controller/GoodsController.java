package com._pi.benepick.domain.goods.controller;

import com._pi.benepick.domain.goods.dto.GoodsRequest;
import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.goods.service.GoodsCommandService;
import com._pi.benepick.domain.goods.service.GoodsQueryService;
import com._pi.benepick.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/goods")
@Tag(name = "Goods", description = "상품 API")
public class GoodsController {

    private final GoodsQueryService goodsQueryService;
    private final GoodsCommandService goodsCommandService;

    //상품 목록 조회
    @Operation(summary = "상품 목록 조회 (관리자용)", description = "상품의 모든 목록을 조회합니다.(진행:PROGRESS,예정:SCHEDULED,종료:COMPLETED)")
    @GetMapping("/list")
    public ApiResponse<GoodsResponse.GoodsListResponseDTO> getGoodsList(@RequestParam Integer page, @RequestParam Integer size, @RequestParam String keyword) {
        return ApiResponse.onSuccess(goodsQueryService.getGoodsList());
    }

    //상품 상세 조회
    @Operation(summary = "상품 상세 조회", description = "상품의 상세 정보를 조회합니다.(진행:PROGRESS,예정:SCHEDULED,종료:COMPLETED)")
    @GetMapping("/{goods_id}")
    public ApiResponse<GoodsResponse.GoodsDetailResponseDTO> getGoodsInfo(@PathVariable Long goods_id) {
        return ApiResponse.onSuccess(goodsQueryService.getGoodsInfo(goods_id));
    }

    //시드 값 조회
    @Operation(summary = "시드값 조회", description = "상품의 시드값을 조회합니다.")
    @GetMapping("/seeds/{goods_id}")
    public ApiResponse<GoodsResponse.GoodsSeedsResponseDTO> getSeeds(@PathVariable Long goods_id) {
        return ApiResponse.onSuccess(goodsQueryService.getSeeds(goods_id));
    }

    //상품 추가
    @Operation(summary = "상품 추가", description = "상품을 추가합니다.")
    @PostMapping("/add")
    public ApiResponse<GoodsResponse.GoodsDetailResponseDTO> addGoods(@RequestBody GoodsRequest.GoodsRequestDTO goodsAddDTO) {
        return ApiResponse.onSuccess(goodsCommandService.addGoods(goodsAddDTO));
    }

    //상품 수정
    @Operation(summary = "상품 수정", description = "상품 상세 정보를 수정합니다.")
    @PostMapping("/update/{goodsId}")
    public ApiResponse<GoodsResponse.GoodsDetailResponseDTO> updateGoods(@PathVariable Long goodsId, @RequestBody GoodsRequest.GoodsRequestDTO goodsUpdateDTO) {
        return ApiResponse.onSuccess(goodsCommandService.updateGoods(goodsId, goodsUpdateDTO));
    }

    //상품 삭제
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    @DeleteMapping("/delete")
    public ApiResponse<GoodsResponse.GoodsDeleteResponseDTO> deleteGoods(@RequestParam List<Long> deleteList) {
        return ApiResponse.onSuccess(goodsCommandService.deleteGoods(deleteList));
    }
}
