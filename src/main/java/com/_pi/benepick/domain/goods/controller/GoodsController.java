package com._pi.benepick.domain.goods.controller;

import com._pi.benepick.domain.goods.dto.GoodsRequest;
import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goods.service.GoodsCommandService;
import com._pi.benepick.domain.goods.service.GoodsQueryService;
import com._pi.benepick.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Operation(summary = "상품 목록 조회 (관리자용)", description = "검색어에 따른 상품의 모든 목록을 조회합니다. (진행:PROGRESS,예정:SCHEDULED,종료:COMPLETED)")
    @GetMapping("/list")
    public ApiResponse<GoodsResponse.GoodsListResponseDTO> getGoodsList(@RequestParam Integer page, @RequestParam Integer size, @RequestParam(required = false) String keyword) {
        return ApiResponse.onSuccess(goodsQueryService.getGoodsList(page, size, keyword));
    }

    //상품 상세 조회
    @Operation(summary = "상품 상세 조회 - Mockup API", description = "상품의 상세 정보를 조회합니다.(진행:PROGRESS,예정:SCHEDULED,종료:COMPLETED)")
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

    //상품 검색
    @Operation(summary = "상품 검색 (메인페이지용) - Mockup API", description = "선택된 필터,카테고리,검색어를 기반으로 상품을 조회합니다. (종료임박순:END,인기순:POPULAR,최신순:NEWEST)")
    @GetMapping("/search/{goods_status}")
    public ApiResponse<GoodsResponse.GoodsListSearchResponseDTO> searchGoods(@PathVariable GoodsStatus goods_status, @RequestParam Integer page, @RequestParam Integer size, @RequestParam String keyword, @RequestParam String sortBy, @RequestParam String category) {
        return ApiResponse.onSuccess(goodsQueryService.searchGoods());
    }

    //상품 추가
    @Operation(summary = "상품 추가", description = "상품을 추가합니다.")
    @PostMapping("/add")
    public ApiResponse<GoodsResponse.GoodsDetailResponseDTO> addGoods(@RequestBody GoodsRequest.GoodsRequestDTO goodsAddDTO) {
        return ApiResponse.onSuccess(goodsCommandService.addGoods(goodsAddDTO));
    }

    //상품 파일 업로드
    @Operation(summary = "상품 파일 업로드", description = "엑셀 파일을 업로드하여 상품 정보를 저장합니다.")
    @PostMapping(value ="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<GoodsResponse.GoodsUploadResponseDTO> uploadGoodsFile(@RequestPart("file") MultipartFile file) {
        return ApiResponse.onSuccess(goodsCommandService.uploadGoodsFile(file));
    }

    //상품 수정
    @Operation(summary = "상품 수정", description = "상품 상세 정보를 수정합니다.")
    @PostMapping("/update/{goods_id}")
    public ApiResponse<GoodsResponse.GoodsDetailResponseDTO> updateGoods(@PathVariable Long goods_id, @RequestBody GoodsRequest.GoodsRequestDTO goodsUpdateDTO) {
        return ApiResponse.onSuccess(goodsCommandService.updateGoods(goods_id, goodsUpdateDTO));
    }

    //상품 삭제
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    @DeleteMapping("/delete")
    public ApiResponse<GoodsResponse.GoodsDeleteResponseDTO> deleteGoods(@RequestParam List<Long> deleteList) {
        return ApiResponse.onSuccess(goodsCommandService.deleteGoods(deleteList));
    }
}
