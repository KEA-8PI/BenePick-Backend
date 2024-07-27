package com._pi.benepick.domain.raffles.controller;

import com._pi.benepick.domain.raffles.dto.RafflesResponse;
import com._pi.benepick.domain.raffles.service.RafflesQueryServiceImpl;
import com._pi.benepick.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/raffles")
@Tag(name = "Raffles", description = "응모 API")
public class RafflesController {

    private final RafflesQueryServiceImpl rafflesQueryService;

    // 인증인가 구현되고 token 받게 되면 수정 예정
    @Operation(summary = "사원별 진행중인 응모 내역 조회", description = "사원이 응모한 응모 진행중인 상품들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/view/member/progress/{memberId}")
    public ApiResponse<RafflesResponse.RafflesResponseByMembersListDTO> getProgressRafflesByMemberId(@PathVariable String memberId) {
        return ApiResponse.onSuccess(rafflesQueryService.getProgressRafflesByMemberId(memberId));
    }

    // 인증인가 구현되고 token 받게 되면 수정 예정
    @Operation(summary = "사원별 종료된 응모 내역 조회", description = "사원이 응모한 추첨 종료된 상품들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/view/member/complete/{memberId}")
    public ApiResponse<RafflesResponse.RafflesResponseByMembersListDTO> getCompleteRafflesByMemberId(@PathVariable String memberId) {
        return ApiResponse.onSuccess(rafflesQueryService.getCompleteRafflesByMemberId(memberId));
    }

    @Operation(summary = "상품별 응모자 조회", description = "상품에 응모한 사원들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/view/goods/applicant/{goodsId}")
    public ApiResponse<RafflesResponse.RafflesResponseByGoodsListDTO> getAllRafflesByGoodsId(@PathVariable Long goodsId) {
        return ApiResponse.onSuccess(rafflesQueryService.getAllRafflesByGoodsId(goodsId));
    }

    @Operation(summary = "상품별 대기자 조회", description = "상품에 응모한 사원들 중 추첨 결과가 대기중인 사원들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/view/goods/waitlist/{goodsId}")
    public ApiResponse<RafflesResponse.RafflesResponseByGoodsListDTO> getWaitlistRafflesByGoodsId(@PathVariable Long goodsId) {
        return ApiResponse.onSuccess(rafflesQueryService.getWaitlistRafflesByGoodsId(goodsId));
    }

    @Operation(summary = "상품별 당첨자 조회", description = "상품에 응모한 사원들 중 추첨 결과가 대기가 아닌 사원들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/view/goods/winners/{goodsId}")
    public ApiResponse<RafflesResponse.RafflesResponseByGoodsListDTO> getWinnersRafflesByGoodsId(@PathVariable Long goodsId) {
        return ApiResponse.onSuccess(rafflesQueryService.getWinnersRafflesByGoodsId(goodsId));
    }

}