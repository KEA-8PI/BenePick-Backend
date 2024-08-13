package com._pi.benepick.domain.draws.controller;

import com._pi.benepick.domain.draws.dto.DrawsRequest;
import com._pi.benepick.domain.draws.service.DrawsComposeService;
import com._pi.benepick.domain.draws.service.DrawsQueryService;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.global.common.annotation.MemberObject;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import com._pi.benepick.domain.draws.dto.DrawsResponse.*;
import com._pi.benepick.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/draws")
@Tag(name = "Draws", description = "추첨 API")
public class DrawsController {

    private final DrawsComposeService drawsComposeService;
    private final DrawsQueryService drawsQueryService;

    @Operation(summary = "상품별 결과 조회", description = "상품의 추첨 결과가 당첨자인 사원들과 그 내역을 확인할 수 있습니다.(사용자 페이지)")
    @GetMapping("/result/{goodsId}")
    public ApiResponse<DrawsResponseByGoodsListDTO> getResultByGoodsId(@PathVariable Long goodsId) {
        return ApiResponse.onSuccess(drawsQueryService.getResultByGoodsId(goodsId));
    }

    @Operation(summary = "상품별 당첨자 조회", description = "상품에 응모한 사원들 중 추첨 결과가 대기 & 낙첨이 아닌 사원들과 그 내역을 확인할 수 있습니다.(관리자 페이지)")
    @GetMapping("/winners/{goodsId}")
    public ApiResponse<DrawsResponseByWinnerGoodsIdListDTO> getWinnersByGoodsId(@Parameter(hidden = true) @MemberObject Members member, @PathVariable Long goodsId) {
        return ApiResponse.onSuccess(drawsQueryService.getWinnersByGoodsId(member, goodsId));
    }

    @Operation(summary = "상품별 대기자 조회", description = "상품에 응모한 사원들 중 추첨 결과가 대기중인 사원들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/waitlist/{goodsId}")
    public ApiResponse<DrawsResponseByWaitlistGoodsIdListDTO> getWaitlistByGoodsId(@Parameter(hidden = true) @MemberObject Members member,@PathVariable Long goodsId) {
        return ApiResponse.onSuccess(drawsQueryService.getWaitlistByGoodsId(member, goodsId));
    }

    @Operation(summary = "사원별 종료된 응모 내역 조회", description = "사원이 응모한 추첨 종료된 상품들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/complete/list")
    public ApiResponse<DrawsResponseByMembersListDTO> getCompleteRafflesByMemberId(@Parameter(hidden = true) @MemberObject Members member) {
        return ApiResponse.onSuccess(drawsQueryService.getCompleteRafflesByMemberId(member));
    }

    @Operation(summary = "당첨자 상태 관리", description = "당첨자들의 상태를 관리할 수 있습니다.")
    @PatchMapping("/winners/edit/{winnersId}")
    public ApiResponse<EditWinnerStatus> editWinnerStatus(@Parameter(hidden = true) @MemberObject Members member,@PathVariable Long winnersId, @RequestBody DrawsRequest.DrawsRequestDTO dto) {
        return ApiResponse.onSuccess(drawsComposeService.editWinnerStatus(member, winnersId, dto));
    }

    @Operation(summary = "추첨 결과 다운로드", description = "추첨 결과가 정리된 엑셀 파일을 다운로드 할 수 있습니다.")
    @GetMapping("/download/{goodsId}")
    public void downloadExcel(@Parameter(hidden = true) @MemberObject Members member, @PathVariable Long goodsId, HttpServletResponse response) {
        drawsQueryService.downloadExcel(member, goodsId, response);
    }

    @Operation(summary = "추첨 검증", description = "시드값을 이용하여 추첨 로직 검증을 할 수 있습니다.")
    @GetMapping("/verification/{goodsId}")
    public ApiResponse<DrawsResponseResultListDTO> verificationSeed(@PathVariable Long goodsId, @RequestParam String seed) {
        return ApiResponse.onSuccess(drawsComposeService.verificationSeed(goodsId, seed));
    }

}
