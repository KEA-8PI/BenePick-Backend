package com._pi.benepick.domain.members.controller;
import com._pi.benepick.domain.members.dto.MembersRequest.*;
import com._pi.benepick.domain.members.dto.MembersResponse.*;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.repository.MembersRepository;

import com._pi.benepick.domain.members.service.MembersQueryService;

import com._pi.benepick.domain.members.service.MembersCommandService;
import com._pi.benepick.global.common.annotation.MemberObject;
import com._pi.benepick.global.common.exception.ApiException;

import com._pi.benepick.global.common.response.ApiResponse;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
@Tag(name = "Members", description = "사원 API")
public class MembersController {



    private final MembersCommandService membersCommandService;
    private final MembersQueryService membersQueryService;
    private final MembersRepository membersRepository;



    @Operation(summary = "복지포인트 조회 ", description = "사용자가 복지포인트를 조회합니다.")
    @GetMapping("/point")
    public ApiResponse<MemberPointDTO> getMemberpoint(@Parameter(hidden = true) @MemberObject Members member){
        return ApiResponse.onSuccess(membersQueryService.getMemberPoint(member));

    }

    @Operation(summary = "내 정보 조회", description = "사용자가 본인 정보를 조회합니다.")
    @GetMapping("/info")
    public ApiResponse<MembersDetailResponseDTO> getMemberInfo(@Parameter(hidden = true) @MemberObject Members member){
        return ApiResponse.onSuccess(membersQueryService.getMemberinfo(member.getId()));

    }

    @Operation(summary = "비밀번호 변경", description = "사용자가 비밀번호를 변경합니다.")
    @PatchMapping("/password")
    public ApiResponse<MembersuccessDTO> updatePassword(@Parameter(hidden = true) @MemberObject Members member,@RequestBody MemberPasswordDTO memberPasswordDTO){
return ApiResponse.onSuccess(membersCommandService.changePassword(memberPasswordDTO,member));
    }

    @Operation(summary = "사원 목록 조회 및 검색", description = "사원 목록을 조회하고 검색합니다 (관리자용)")
    @GetMapping("/list")
    public ApiResponse<MembersDetailListResponseDTO> getMemberList(@RequestParam Integer page, @RequestParam Integer size, @RequestParam(required = false) String keywordName){
        return ApiResponse.onSuccess(membersQueryService.getMembersList(page,size,keywordName));


    }

    @Operation(summary = "사원 등록", description = "사원을 등록합니다 (관리자용)")
    @PostMapping("/add")
    public ApiResponse<MembersDetailResponseDTO> addMember(@Parameter(hidden = true) @MemberObject Members member,@RequestBody AdminMemberRequestDTO membersRequestDTO){
        return ApiResponse.onSuccess(membersCommandService.addMembers(membersRequestDTO,member));
    }

    @Operation(summary = "복지포인트 파일 업로드", description = "복지 포인트 파일을 업로드합니다. (관리자용)")
    @PostMapping(value="/point/upload",consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<MembersDetailListResponseDTO> uploadPointFile(@RequestPart("file") MultipartFile file) throws IOException {
        return  ApiResponse.onSuccess(membersCommandService.uploadPointFile(file));
    }

    @Operation(summary = "사원 정보 수정", description = "사원 정보를 수정합니다. (관리자용)")
    @PatchMapping("/info/{memberID}")
    public ApiResponse<updateMemberResponseDTO> updateMemberInfo(@Parameter(hidden = true) @MemberObject Members member,@PathVariable String memberID, @RequestBody MembersRequestDTO membersRequestDTO){
        return ApiResponse.onSuccess(membersCommandService.updateMemberInfo(memberID,membersRequestDTO,member));
    }

    @Operation(summary = "사원 삭제",description = "사원을 삭제합니다. (관리자용)")
    @DeleteMapping("/")
    public ApiResponse<DeleteResponseDTO> deleteMember(@Parameter(hidden = true) @MemberObject Members member,@RequestBody DeleteMembersRequestDTO deleteMembersRequestDTO){
        return ApiResponse.onSuccess(membersCommandService.deleteMembers(deleteMembersRequestDTO,member));

    }

    @Operation(summary = "사원 추가 파일 등록", description = "복지 포인트 파일을 업로드합니다. (관리자용)")
    @PostMapping(value="/add/upload",consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<MembersDetailListResponseDTO> uploadMemberFile(@RequestPart("file") MultipartFile file){
        return ApiResponse.onSuccess(membersCommandService.uploadMemberFile(file));
    }

}
