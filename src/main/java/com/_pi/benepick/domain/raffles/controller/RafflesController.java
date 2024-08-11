package com._pi.benepick.domain.raffles.controller;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.raffles.dto.RafflesRequest;
import com._pi.benepick.domain.raffles.dto.RafflesResponse;
import com._pi.benepick.domain.raffles.service.RafflesCommandService;
import com._pi.benepick.domain.raffles.service.RafflesQueryService;
import com._pi.benepick.global.common.annotation.MemberObject;
import com._pi.benepick.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/raffles")
@Tag(name = "Raffles", description = "응모 API")
public class RafflesController {

    private final RafflesQueryService rafflesQueryService;
    private final RafflesCommandService rafflesCommandService;

    @Operation(summary = "응모하기", description = "물품 아이디, 포인트를 사용하여 응모합니다.")
    @PostMapping("/apply/{goodsId}")
    public ApiResponse<RafflesResponse.RafflesResponseByGoodsDTO> getApplyRaffleByGoodsId(@Parameter(hidden = true) @MemberObject Members member, @PathVariable Long goodsId, @RequestBody RafflesRequest.RafflesRequestDTO raffleAddDTO) {
        return ApiResponse.onSuccess(rafflesCommandService.applyRaffle(member, goodsId, raffleAddDTO));
    }

    @Operation(summary = "사원별 진행중인 응모 내역 조회", description = "사원이 응모한 응모 진행중인 상품들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/progress/member")
    public ApiResponse<RafflesResponse.RafflesResponseByMembersListDTO> getProgressRafflesByMemberId(@Parameter(hidden = true) @MemberObject Members member) {
        return ApiResponse.onSuccess(rafflesQueryService.getProgressRafflesByMemberId(member));
    }

    @Operation(summary = "상품별 응모자 조회", description = "상품에 응모한 사원들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/applicant/{goodsId}")
    public ApiResponse<RafflesResponse.RafflesResponseByGoodsListDTO> getAllRafflesByGoodsId(@Parameter(hidden = true) @MemberObject Members member,@PathVariable Long goodsId) {
        return ApiResponse.onSuccess(rafflesQueryService.getAllRafflesByGoodsId(member, goodsId));
    }
}