package com._pi.benepick.domain.raffles.controller;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.raffles.dto.RafflesRequest.RafflesRequestDTO;
import com._pi.benepick.domain.raffles.dto.RafflesResponse.*;
import com._pi.benepick.domain.raffles.service.RafflesComposeService;
import com._pi.benepick.domain.raffles.service.RafflesQueryService;
import com._pi.benepick.global.common.annotation.MemberObject;
import com._pi.benepick.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/raffles")
@Tag(name = "Raffles", description = "응모 API")
public class RafflesController {

    private final RafflesComposeService rafflesComposeService;
    private final RafflesQueryService rafflesQueryService;

    @Operation(summary = "응모하기", description = "물품 아이디, 포인트를 사용하여 응모합니다.")
    @PostMapping("/apply/{goodsId}")
    public ApiResponse<RafflesResponseByGoodsDTO> getApplyRaffleByGoodsId(@Parameter(hidden = true) @MemberObject Members member, @PathVariable Long goodsId, @Valid @RequestBody RafflesRequestDTO raffleAddDTO) {
        return ApiResponse.onSuccess(rafflesComposeService.applyRaffle(member, goodsId, raffleAddDTO));
    }

    @Operation(summary = "사원별 진행중인 응모 내역 조회", description = "사원이 응모한 응모 진행중인 상품들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/progress/member")
    public ApiResponse<RafflesResponseByMembersListDTO> getProgressRafflesByMemberId(@Parameter(hidden = true) @MemberObject Members member) {
        return ApiResponse.onSuccess(rafflesQueryService.getProgressRafflesByMemberId(member));
    }

    @Operation(summary = "상품별 응모자 조회", description = "상품에 응모한 사원들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/applicant/{goodsId}")
    public ApiResponse<RafflesResponseByGoodsListDTO> getAllRafflesByGoodsId(@Parameter(hidden = true) @MemberObject Members member,@PathVariable Long goodsId) {
        return ApiResponse.onSuccess(rafflesComposeService.getAllRafflesByGoodsId(member, goodsId));
    }

    @Operation(summary = "응모 현황 정보", description = "상품에 대한 응모 정보들을 확인할 수 있습니다.")
    @GetMapping("/current/state/{goodsId}")
    public ApiResponse<CurrentStateByGoodsListDTO> getCurrentStateByGoodsId(@PathVariable Long goodsId) {
        return ApiResponse.onSuccess(rafflesComposeService.getCurrentStateByGoods(goodsId));
    }
}