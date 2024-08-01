package com._pi.benepick.domain.members.controller;


import com._pi.benepick.domain.members.dto.MembersRequest.*;
import com._pi.benepick.domain.members.dto.MembersResponse.*;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.domain.members.service.MembersCommandService;
import com._pi.benepick.domain.members.service.MembersQueryService;
import com._pi.benepick.domain.penaltyHists.dto.PenaltyResponse.*;
import com._pi.benepick.domain.penaltyHists.service.PenaltyHistsQueryService;
import com._pi.benepick.domain.pointHists.dto.PointResponse.*;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.ApiResponse;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private final PenaltyHistsQueryService penaltyHistsQueryService;
    @Operation(summary = "복지포인트 조회 - Mockup API", description = "사용자가 복지포인트를 조회합니다.")
    @GetMapping("/point")
    public ApiResponse<MemberPointDTO> getMemberpoint(){
        MemberPointDTO memberPointDTO = new MemberPointDTO();
        memberPointDTO.setId("thwjd0808");
        memberPointDTO.setPoint((long)100);

        return ApiResponse.onSuccess(memberPointDTO);

    }

    @Operation(summary = "내 정보 조회", description = "사용자가 본인 정보를 조회합니다.")
    @GetMapping("/info")
    public ApiResponse<MembersDetailResponseDTO> getMemberInfo(){
        Members members=membersRepository.findById("string").orElseThrow(()->new ApiException(ErrorStatus._MEMBERS_NOT_FOUND));
        return ApiResponse.onSuccess(membersQueryService.getMemberinfo(members.getId()));

    }


    @Operation(summary = "복지포인트내역 조회 - Mockup API", description = "사용자가 본인의 포인트 사용 내역을 조회합니다.")
    @GetMapping("/point-hist")
    public ApiResponse<PointHistListDTO> getMemberpointInfo(){
       PointHistDTO pointHistDTO1=new PointHistDTO((long)100,"macbook pro 응모");
        PointHistDTO pointHistDTO2=new PointHistDTO((long)200,"호텔 숙박권 응모");
       List<PointHistDTO> pointHistDTOS=Arrays.asList(pointHistDTO1,pointHistDTO2);

       return ApiResponse.onSuccess(PointHistListDTO.builder()
               .pointHistDTOS(pointHistDTOS)
               .build());
    }



    @Operation(summary = "비밀번호 변경 - Mockup API", description = "사용자가 비밀번호를 변경합니다.")
    @PatchMapping("/password")
    public ApiResponse<MembersuccessDTO> updatePassword(@RequestBody MemberPasswordDTO memberPasswordDTO){
return ApiResponse.onSuccess(MembersuccessDTO.builder()
                .msg("수정되었습니다.")
        .build());
    }

    @Operation(summary = "사원 목록 조회 및 검색 - Mockup API", description = "사원 목록을 조회하고 검색합니다 (관리자용)")
    @GetMapping("/list")
    public ApiResponse<MembersDetailListResponseDTO> getMemberList(@RequestParam Integer page, @RequestParam Integer size, @RequestParam String keywordName){

        return ApiResponse.onSuccess(membersQueryService.getMembersList(page,size,keywordName));

    }

    @Operation(summary = "사원 등록", description = "사원을 등록합니다 (관리자용)")
    @PostMapping("/add")
    public ApiResponse<MembersDetailResponseDTO> addMember(@RequestBody AdminMemberRequestDTO membersRequestDTO){
        Members members=membersRepository.findById("string").orElseThrow(()->new ApiException(ErrorStatus._MEMBERS_NOT_FOUND));
        return ApiResponse.onSuccess(membersCommandService.addMembers(membersRequestDTO,members));
    }

    @Operation(summary = "복지포인트 파일 업로드 - Mockup API", description = "복지 포인트 파일을 업로드합니다. (관리자용)")
    @PostMapping(value="/point/upload",consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<MembersuccessDTO> uploadPointFile(@RequestPart("file") MultipartFile file){
        return ApiResponse.onSuccess(MembersuccessDTO.builder()
                .msg("수정되었습니다.")
                .build());
    }

    @Operation(summary = "사원 정보 수정 - Mockup API", description = "사원 정보를 수정합니다. (관리자용)")
    @PatchMapping("/info/{memberID}")
    public ApiResponse<MembersuccessDTO> updateMemberInfo(@PathVariable String memberID, @RequestBody MembersRequestDTO membersRequestDTO){
        return ApiResponse.onSuccess(MembersuccessDTO.builder()
                .msg("수정되었습니다.")
                .build());
    }

    @Operation(summary = "사원 삭제 - Mockup API",description = "사원을 삭제합니다. (관리자용)")
    @DeleteMapping("/{memberId}")
    public ApiResponse<DeleteResponseDTO> deleteMember(@PathVariable String memberId){
        return ApiResponse.onSuccess(DeleteResponseDTO.builder()
                        .msg("삭제되었습니다.")
                .build());
    }

    @Operation(summary = "사원 추가 파일 등록 - Mockup API", description = "복지 포인트 파일을 업로드합니다. (관리자용)")
    @PostMapping(value="/add/upload",consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<MemberIDListResponseDTO> uploadMemberFile(@RequestPart("file") MultipartFile file){

        String id="123";
        String id1="456";
        List<String> membersList = Arrays.asList(id, id);
        return ApiResponse.onSuccess(MemberIDListResponseDTO.builder()
                        .id(membersList)
                .build());
    }

}
