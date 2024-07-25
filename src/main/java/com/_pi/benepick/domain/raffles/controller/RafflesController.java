package com._pi.benepick.domain.raffles.controller;

import com._pi.benepick.domain.raffles.dto.RafflesResponse;
import com._pi.benepick.domain.raffles.service.RafflesQueryServiceImpl;
import com._pi.benepick.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/raffles")
@Tag(name = "Raffle", description = "응모 API")
public class RafflesController {

    private final RafflesQueryServiceImpl rafflesQueryService;

    @Operation(summary = "상품별 응모 조회", description = "상품에 응모한 사원들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/goods/{goodsId}")
    public ApiResponse<List> getAllRafflesByGoodsId(@PathVariable Long goodsId) {
        return ApiResponse.onSuccess(rafflesQueryService.getAllRafflesByGoodsId(goodsId));
    }

    @Operation(summary = "상품별 응모 조회", description = "사원이 응모한 상품들과 그 내역을 확인할 수 있습니다..")
    @GetMapping("/members/{memberId}")
    public ApiResponse<List> getAllRafflesByMemberId(@PathVariable String memberId) {
        return ApiResponse.onSuccess(rafflesQueryService.getAllRafflesByMemberId(memberId));
    }
}