package com._pi.benepick.domain.draws.controller;

import com._pi.benepick.domain.draws.dto.DrawsRequest;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com._pi.benepick.domain.draws.dto.DrawsResponse;
import com._pi.benepick.domain.draws.entity.Status;
import com._pi.benepick.domain.draws.service.DrawsQueryService;
import com._pi.benepick.domain.raffles.controller.RafflesController;
import com._pi.benepick.global.common.response.ApiResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/draws")
@Tag(name = "Draws", description = "추첨 API")
public class DrawsController {

    private final DrawsQueryService drawsQueryService;
    private final MembersRepository membersRepository;

    @Operation(summary = "상품별 당첨자 조회", description = "상품에 응모한 사원들 중 추첨 결과가 대기가 아닌 사원들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/winners/{goodsId}")
    public ApiResponse<DrawsResponse.DrawsResponseByGoodsListDTO> getWinnersByGoodsId(@PathVariable Long goodsId) {
        return ApiResponse.onSuccess(drawsQueryService.getWinnersByGoodsId(goodsId));
    }

    @Operation(summary = "상품별 대기자 조회", description = "상품에 응모한 사원들 중 추첨 결과가 대기중인 사원들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/waitlist/{goodsId}")
    public ApiResponse<DrawsResponse.DrawsResponseByGoodsListDTO> getWaitlistByGoodsId(@PathVariable Long goodsId) {
        return ApiResponse.onSuccess(drawsQueryService.getWaitlistByGoodsId(goodsId));
    }

    // 인증인가 구현되고 token 받게 되면 수정 예정
    @Operation(summary = "사원별 종료된 응모 내역 조회", description = "사원이 응모한 추첨 종료된 상품들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/complete/list/{memberId}")
    public ApiResponse<DrawsResponse.DrawsResponseByMembersListDTO> getCompleteRafflesByMemberId(@PathVariable String memberId) {
        return ApiResponse.onSuccess(drawsQueryService.getCompleteRafflesByMemberId(memberId));
    }

    @Operation(summary = "당첨자 상태 관리 - Mockup API", description = "당첨자들의 상태를 관리할 수 있습니다.")
    @PatchMapping("/winners/edit/{winnersId}")
    public ApiResponse<DrawsResponse.DrawsResponseByMembersDTO> editWinnerStatus(@PathVariable Long winnersId, @RequestBody DrawsRequest.DrawsRequestDTO dto) {
        Members member = membersRepository.findById("string").orElseThrow(() -> new ApiException(ErrorStatus._UNAUTHORIZED));
        return ApiResponse.onSuccess(drawsQueryService.editWinnerStatus(member, winnersId, dto));
    }

    @Operation(summary = "추첨 결과 다운로드 - Mockup API", description = "추첨 결과가 정리된 엑셀 파일을 다운로드 할 수 있습니다.")
    @GetMapping("/download/{goodsId}")
    public void downloadExcel(@PathVariable Long goodsId, HttpServletResponse response) throws IOException {
        Members member = membersRepository.findById("string").orElseThrow(() -> new ApiException(ErrorStatus._UNAUTHORIZED));
        drawsQueryService.downloadExcel(member, goodsId, response);
    }

}
