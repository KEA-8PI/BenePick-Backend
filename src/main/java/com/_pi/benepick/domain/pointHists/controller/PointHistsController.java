package com._pi.benepick.domain.pointHists.controller;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.domain.members.service.MembersQueryService;
import com._pi.benepick.domain.pointHists.dto.PointResponse;
import com._pi.benepick.global.common.annotation.MemberObject;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.ApiResponse;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "PointHists", description = "포인트 히스토리 API")
@RequestMapping("/point-hists")
public class PointHistsController {
    private final MembersRepository membersRepository;
    private final MembersQueryService membersQueryService;
    @Operation(summary = "복지포인트내역 조회", description = "사용자가 본인의 포인트 사용 내역을 조회합니다.")
    @GetMapping("/")
    public ApiResponse<PointResponse.PointHistListDTO> getMemberpointInfo(@Parameter(hidden = true) @MemberObject Members member){
        return ApiResponse.onSuccess(membersQueryService.getPointHist(member));
    }

}