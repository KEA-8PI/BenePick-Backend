package com._pi.benepick.domain.members.controller;

import com._pi.benepick.domain.members.dto.MembersRequest.*;
import com._pi.benepick.domain.members.dto.MembersResponse.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/")
@Tag(name = "Admin", description = "사원 관리 API")
public class AdminController {

    @Operation(summary = "사원 목록 조회 및 검색", description = "mockup API")
    @GetMapping("/admin/member/list")
    public MembersDetailListResponseDTO getMemberList(@RequestParam Integer page, @RequestParam Integer size, @RequestParam String keywordID, @RequestParam String keywordName  ){
        MembersDetailResponseDTO member1 = new MembersDetailResponseDTO("john.doe@example.co", "John Doe", "기획팀",(long)100,(long)5);
        MembersDetailResponseDTO member2 = new MembersDetailResponseDTO("jane.smith@example.com", "Jane Smith", "기획팀",(long)100,(long)5);
        MembersDetailResponseDTO member3 = new MembersDetailResponseDTO("mike.johnson@example.com", "Mike Johnson","기획팀",(long)100,(long)5);

        List<MembersDetailResponseDTO> membersList = Arrays.asList(member1, member2, member3);


        MembersDetailListResponseDTO membersDetailListResponseDTO = MembersDetailListResponseDTO.builder()
                .membersDetailResponseDTOList(membersList)
                .build();
        return membersDetailListResponseDTO;
    }

    @Operation(summary = "사원 등록", description = "mockup API")
    @PostMapping("/admin/member/add")
    public String addMember(@RequestBody MembersRequestDTO membersRequestDTO){
        return "추가되었습니다";
    }

    @Operation(summary = "복지포인트 파일 업로드", description = "mockup API")
    @PostMapping("/admin/point/upload")
    public String uploadPointFile(@Valid @Parameter(
            content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE) ) MemberPointFileDTO uploadFileDTO){
        MultipartFile file = uploadFileDTO.getFile();
        return "추가되었습니다";
    }

    @Operation(summary = "사원 정보 수정", description = "mockup API")
    @PatchMapping("/admin/update/{memberID}")
    public String updateMemberInfo(@PathVariable String memberID, @RequestBody MembersRequestDTO membersRequestDTO){
        return "수정되었습니다";
    }
}
