package com._pi.benepick.domain.penaltyHists.controller;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.penaltyHists.dto.PenaltyResponse.*;
import com._pi.benepick.domain.penaltyHists.service.PenaltyHistsQueryService;
import com._pi.benepick.global.common.annotation.MemberObject;
import com._pi.benepick.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "PenaltyHists", description = "패널티 히스토리 API")
@RequestMapping("/penalty-hists")
public class PenaltyHistsController {
    private final PenaltyHistsQueryService penaltyHistsQueryService;
    @Operation(summary = "패널티내역 조회", description = "사용자가 본인의 패널티 내역을 조회합니다.")
    @GetMapping("/")
    public ApiResponse<PenaltyListResponseDTO> getMemberpenaltyInfo(@RequestParam Integer page, @RequestParam Integer size, @Parameter(hidden = true) @MemberObject Members member){
        return ApiResponse.onSuccess(penaltyHistsQueryService.getPenaltyHists(page,size,member));
    }

}