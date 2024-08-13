package com._pi.benepick.domain.goods.controller;

import com._pi.benepick.domain.goods.dto.GoodsRequest;
import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.goods.entity.GoodsFilter;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goods.service.GoodsComposeService;
import com._pi.benepick.domain.goods.service.GoodsQueryService;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.global.common.annotation.MemberObject;
import com._pi.benepick.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/goods")
@Tag(name = "Goods", description = "상품 API")
public class GoodsController {

    private final GoodsQueryService goodsQueryService;
    private final GoodsComposeService goodsComposeService;

    // 유효성 검사가 실패한 경우 예외를 처리하는 핸들러
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    //상품 목록 조회 (관리자)
    @Operation(summary = "상품 목록 조회 (관리자용)", description = "검색어에 따른 상품의 모든 목록을 조회합니다. (진행:PROGRESS,예정:SCHEDULED,종료:COMPLETED)")
    @GetMapping("/list")
    public ApiResponse<GoodsResponse.GoodsListResponseDTO> getGoodsList(@Parameter(hidden = true) @MemberObject Members member,
                                                                        @RequestParam Integer page, @RequestParam Integer size,
                                                                        @RequestParam(required = false) String keyword) {
        return ApiResponse.onSuccess(goodsQueryService.getGoodsList(page, size, keyword, member));
    }

    //상품 상세 조회
    @Operation(summary = "상품 상세 조회", description = "상품의 상세 정보를 조회합니다.(진행:PROGRESS,예정:SCHEDULED,종료:COMPLETED)")
    @GetMapping("/{goodsId}")
    public ApiResponse<GoodsResponse.GoodsDetailResponseDTO> getGoodsInfo(@Parameter(hidden = true) @MemberObject Members member, @PathVariable Long goodsId) {
        return ApiResponse.onSuccess(goodsComposeService.getGoodsInfo(goodsId, member));
    }

    //시드 값 조회
    @Operation(summary = "시드값 조회", description = "상품의 시드값을 조회합니다.")
    @GetMapping("/seeds/{goodsId}")
    public ApiResponse<GoodsResponse.GoodsSeedsResponseDTO> getSeeds(@PathVariable Long goodsId) {
        return ApiResponse.onSuccess(goodsQueryService.getSeeds(goodsId));
    }

    //상품 검색
    @Operation(summary = "상품 검색 (메인페이지용)", description = "선택된 필터,카테고리,검색어를 기반으로 상품을 조회합니다.")
    @GetMapping("/search/{goodsStatus}")
    public ApiResponse<GoodsResponse.GoodsListSearchResponseDTO> searchGoods(@Parameter(hidden = true) @MemberObject Members member,
                                                                             @PathVariable @Valid GoodsStatus goodsStatus,
                                                                             @RequestParam @Valid Integer page,
                                                                             @RequestParam @Valid Integer size,
                                                                             @RequestParam(required = false) @Valid String keyword,
                                                                             @RequestParam @Valid GoodsFilter sortBy,
                                                                             @RequestParam(required = false ) @Valid String category) {
        return ApiResponse.onSuccess(goodsComposeService.searchGoods(goodsStatus, page, size, keyword, sortBy, category, member));
    }

    //상품 추가 (관리자)
    @Operation(summary = "상품 추가", description = "상품을 추가합니다.")
    @PostMapping("/add")
    public ApiResponse<GoodsResponse.GoodsAddResponseDTO> addGoods(@Parameter(hidden = true) @MemberObject Members member, @RequestBody @Valid GoodsRequest.GoodsRequestDTO goodsAddDTO) {
        return ApiResponse.onSuccess(goodsComposeService.addGoods(goodsAddDTO, member));
    }

    //상품 파일 업로드 (관리자)
    @Operation(summary = "상품 파일 업로드", description = "엑셀 파일을 업로드하여 상품 정보를 저장합니다.")
    @PostMapping(value ="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<GoodsResponse.GoodsUploadResponseDTO> uploadGoodsFile(@Parameter(hidden = true) @MemberObject Members member, @RequestPart("file") @Valid MultipartFile file) {
        return ApiResponse.onSuccess(goodsComposeService.uploadGoodsFile(file, member));
    }

    //상품 수정 (관리자)
    @Operation(summary = "상품 수정", description = "상품 상세 정보를 수정합니다.")
    @PostMapping("/update/{goodsId}")
    public ApiResponse<GoodsResponse.GoodsAddResponseDTO> updateGoods(@Parameter(hidden = true) @MemberObject Members member,
                                                                      @PathVariable @Valid Long goodsId,
                                                                      @RequestBody @Valid GoodsRequest.GoodsRequestDTO goodsUpdateDTO) {
        return ApiResponse.onSuccess(goodsComposeService.updateGoods(goodsId, goodsUpdateDTO, member));
    }

    //상품 삭제 (관리자)
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    @DeleteMapping("/delete")
    public ApiResponse<GoodsResponse.GoodsDeleteResponseDTO> deleteGoods(@Parameter(hidden = true) @MemberObject Members member, @RequestParam @Valid List<Long> deleteList) {
        return ApiResponse.onSuccess(goodsComposeService.deleteGoods(deleteList, member));
    }
}
