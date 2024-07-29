package com._pi.benepick.domain.raffles.controller;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.raffles.dto.RafflesResponse;
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.domain.raffles.service.RafflesQueryService;
import com._pi.benepick.global.common.response.ApiResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/raffles")
@Tag(name = "Raffles", description = "응모 API")
public class RafflesController {

    private final RafflesQueryService rafflesQueryService;

    @Operation(summary = "응모하기 - Mockup API", description = "물품 아이디, 포인트를 사용하여 응모합니다.")
    @PostMapping("/apply/{goodsId}")
    public ApiResponse<Result> getApplyRaffleByGoodsId(@PathVariable Long goodsId, @RequestBody Long point) {

        return ApiResponse.onSuccess(new Result(point, goodsId, LocalDateTime.now()));
    }

    @AllArgsConstructor
    private static class Result {
        @JsonProperty("point")
        private Long point;

        @JsonProperty("goods_id")
        private Long goods_id;

        @JsonProperty("updated_at")
        private LocalDateTime updated_at;
    }

    // 인증인가 구현되고 token 받게 되면 수정 예정
    @Operation(summary = "사원별 진행중인 응모 내역 조회", description = "사원이 응모한 응모 진행중인 상품들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/progress/member/{memberId}")
    public ApiResponse<RafflesResponse.RafflesResponseByMembersListDTO> getProgressRafflesByMemberId(@PathVariable String memberId) {
        return ApiResponse.onSuccess(rafflesQueryService.getProgressRafflesByMemberId(memberId));
    }

    @Operation(summary = "상품별 응모자 조회", description = "상품에 응모한 사원들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/applicant/{goodsId}")
    public ApiResponse<RafflesResponse.RafflesResponseByGoodsListDTO> getAllRafflesByGoodsId(@PathVariable Long goodsId) {
        return ApiResponse.onSuccess(rafflesQueryService.getAllRafflesByGoodsId(goodsId));
    }

}