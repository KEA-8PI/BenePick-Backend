package com._pi.benepick.domain.draws.controller;

import com._pi.benepick.domain.draws.dto.DrawsRequest;
import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.draws.service.DrawsCommandService;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import jakarta.servlet.http.HttpServletResponse;
import com._pi.benepick.domain.draws.dto.DrawsResponse;
import com._pi.benepick.domain.draws.service.DrawsQueryService;
import com._pi.benepick.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/draws")
@Tag(name = "Draws", description = "추첨 API")
public class DrawsController {

    private final DrawsQueryService drawsQueryService;
    private final DrawsCommandService drawsCommandService;
    private final MembersRepository membersRepository;

    @Operation(summary = "상품별 결과 조회", description = "상품의 추첨 결과가 당첨자인 사원들과 그 내역을 확인할 수 있습니다.(사용자 페이지)")
    @GetMapping("/result/{goodsId}")
    public ApiResponse<DrawsResponse.DrawsResponseByGoodsListDTO> getResultByGoodsId(@PathVariable Long goodsId) {
        return ApiResponse.onSuccess(drawsQueryService.getResultByGoodsId(goodsId));
    }

    @Operation(summary = "상품별 당첨자 조회", description = "상품에 응모한 사원들 중 추첨 결과가 대기 & 낙첨이 아닌 사원들과 그 내역을 확인할 수 있습니다.(관리자 페이지)")
    @GetMapping("/winners/{goodsId}")
    public ApiResponse<DrawsResponse.DrawsResponseByWinnerGoodsIdListDTO> getWinnersByGoodsId(@PathVariable Long goodsId) {
        Members member = membersRepository.findById("string").orElseThrow(() -> new ApiException(ErrorStatus._UNAUTHORIZED));
        return ApiResponse.onSuccess(drawsQueryService.getWinnersByGoodsId(member, goodsId));
    }

    @Operation(summary = "상품별 대기자 조회", description = "상품에 응모한 사원들 중 추첨 결과가 대기중인 사원들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/waitlist/{goodsId}")
    public ApiResponse<DrawsResponse.DrawsResponseByWaitlistGoodsIdListDTO> getWaitlistByGoodsId(@PathVariable Long goodsId) {
        Members member = membersRepository.findById("string").orElseThrow(() -> new ApiException(ErrorStatus._UNAUTHORIZED));
        return ApiResponse.onSuccess(drawsQueryService.getWaitlistByGoodsId(member, goodsId));
    }

    // 인증인가 구현되고 token 받게 되면 수정 예정
    @Operation(summary = "사원별 종료된 응모 내역 조회", description = "사원이 응모한 추첨 종료된 상품들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/complete/list/{memberId}")
    public ApiResponse<DrawsResponse.DrawsResponseByMembersListDTO> getCompleteRafflesByMemberId(@PathVariable String memberId) {
        return ApiResponse.onSuccess(drawsQueryService.getCompleteRafflesByMemberId(memberId));
    }

    @Operation(summary = "당첨자 상태 관리", description = "당첨자들의 상태를 관리할 수 있습니다.")
    @PatchMapping("/winners/edit/{winnersId}")
    public ApiResponse<DrawsResponse.EditWinnerStatus> editWinnerStatus(@PathVariable Long winnersId, @RequestBody DrawsRequest.DrawsRequestDTO dto) {
        Members member = membersRepository.findById("string").orElseThrow(() -> new ApiException(ErrorStatus._UNAUTHORIZED));
        return ApiResponse.onSuccess(drawsCommandService.editWinnerStatus(member, winnersId, dto));
    }

    @Operation(summary = "추첨 결과 다운로드", description = "추첨 결과가 정리된 엑셀 파일을 다운로드 할 수 있습니다.")
    @GetMapping("/download/{goodsId}")
    public void downloadExcel(@PathVariable Long goodsId, HttpServletResponse response) {
        Members member = membersRepository.findById("string").orElseThrow(() -> new ApiException(ErrorStatus._UNAUTHORIZED));
        drawsQueryService.downloadExcel(member, goodsId, response);
    }

    @Operation(summary = "추첨 검증", description = "시드값을 이용하여 추첨 로직 검증을 할 수 있습니다.")
    @GetMapping("/verification/{goodsId}/{seed}")
    public ApiResponse<DrawsResponse.DrawsResponseResultListDTO> verificationSeed(@PathVariable Long goodsId, @PathVariable String seed) {
        return ApiResponse.onSuccess(drawsCommandService.verificationSeed(goodsId, seed));
    }

}
