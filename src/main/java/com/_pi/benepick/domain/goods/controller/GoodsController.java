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

    //상품 엑셀 파일로 추가

    //상품 추가
    @Operation(summary = "상품 추가", description = "상품을 추가합니다.")
    @PostMapping("/add")
    public ApiResponse<GoodsResponse.GoodsResponseDTO> addGoods(@RequestBody GoodsRequest.GoodsRequestDTO goodsAddDTO) {
        return ApiResponse.onSuccess(goodsCommandService.addGoods(goodsAddDTO));
    }

    //상품 상세 조회
    @Operation(summary = "상품 상세 조회", description = "상품의 상세 정보를 조회합니다.(진행:PROGRESS,예정:SCHEDULED,종료:COMPLETED)")
    @GetMapping("/{goodsId}")
    public ApiResponse<GoodsResponse.GoodsResponseDTO> getGoodsInfo(@PathVariable Long goodsId) {
        return ApiResponse.onSuccess(goodsQueryService.getGoodsInfo(goodsId));
    }

    //상품 목록 조회
    @Operation(summary = "상품 목록 조회", description = "상품의 모든 목록을 조회합니다.(진행:PROGRESS,예정:SCHEDULED,종료:COMPLETED)")
    @GetMapping("/list")
    public ApiResponse<List> getGoodsList() {
        return ApiResponse.onSuccess(goodsQueryService.getGoodsList());
    }

    //시드 값 조회
    @Operation(summary = "시드값 조회", description = "상품의 시드값을 조회합니다.")
    @GetMapping("/seeds/{goodsId}")
    public ApiResponse<String> getSeeds(@PathVariable Long goodsId) {
        return ApiResponse.onSuccess(goodsQueryService.getSeeds(goodsId));
    }

    //상품 수정
    @Operation(summary = "상품 수정", description = "상품 상세 정보를 수정합니다.")
    @PostMapping("/update/{goodsId}")
    public ApiResponse<GoodsResponse.GoodsResponseDTO> updateGoods(@PathVariable Long goodsId, @RequestBody GoodsRequest.GoodsRequestDTO goodsUpdateDTO) {
        return ApiResponse.onSuccess(goodsCommandService.updateGoods(goodsId, goodsUpdateDTO));
    }

    //상품 삭제
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    @DeleteMapping("/delete")
    public ApiResponse<String> deleteGoods(@RequestBody List<Long> goodsIds) {
        return ApiResponse.onSuccess(goodsCommandService.deleteGoods(goodsIds));
    }
}
