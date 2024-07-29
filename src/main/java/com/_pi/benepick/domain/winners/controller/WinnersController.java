package com._pi.benepick.domain.winners.controller;

import com._pi.benepick.domain.raffles.dto.RafflesResponse;
import com._pi.benepick.domain.winners.dto.WinnersResponse;
import com._pi.benepick.domain.winners.entity.Winners;
import com._pi.benepick.domain.winners.service.WinnersQueryService;
import com._pi.benepick.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/winners")
@Tag(name = "Winners", description = "추첨 API")
public class WinnersController {

    private final WinnersQueryService winnersQueryService;

    @Operation(summary = "상품별 당첨자 조회", description = "상품에 응모한 사원들 중 추첨 결과가 대기가 아닌 사원들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/winners/{goodsId}")
    public ApiResponse<WinnersResponse.WinnersResponseByGoodsListDTO> getWinnersByGoodsId(@PathVariable Long goodsId) {
        return ApiResponse.onSuccess(winnersQueryService.getWinnersByGoodsId(goodsId));
    }

    @Operation(summary = "상품별 대기자 조회", description = "상품에 응모한 사원들 중 추첨 결과가 대기중인 사원들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/waitlist/{goodsId}")
    public ApiResponse<WinnersResponse.WinnersResponseByGoodsListDTO> getWaitlistByGoodsId(@PathVariable Long goodsId) {
        return ApiResponse.onSuccess(winnersQueryService.getWaitlistByGoodsId(goodsId));
    }

    // 인증인가 구현되고 token 받게 되면 수정 예정
    @Operation(summary = "사원별 종료된 응모 내역 조회", description = "사원이 응모한 추첨 종료된 상품들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/complete/raffles/{memberId}")
    public ApiResponse<WinnersResponse.WinnersResponseByMembersListDTO> getCompleteRafflesByMemberId(@PathVariable String memberId) {
        return ApiResponse.onSuccess(winnersQueryService.getCompleteRafflesByMemberId(memberId));
    }

}
