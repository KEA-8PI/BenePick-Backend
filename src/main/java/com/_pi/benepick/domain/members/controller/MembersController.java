package com._pi.benepick.domain.members.controller;


import com._pi.benepick.domain.members.dto.MembersRequest.*;
import com._pi.benepick.domain.members.dto.MembersResponse.*;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.domain.penaltyHists.dto.PenaltyResponse.*;
import com._pi.benepick.domain.pointHists.dto.PointResponse.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
@Tag(name = "Members", description = "사원 API")
public class MembersController {

    private final MembersRepository membersRepository;
    @Operation(summary = "복지포인트 조회", description = "mockup API")
    @GetMapping("/point")
    public MemberPointDTO getMemberpoint(){
        MemberPointDTO memberPointDTO = new MemberPointDTO();
        memberPointDTO.setId("thwjd0808");
        memberPointDTO.setPoint((long)100);

        return memberPointDTO;

    }

    @Operation(summary = "내 정보 조회", description = "mockup API")
    @GetMapping("/memberInfo")
    public MembersDetailResponseDTO getMemberInfo(){
        MembersDetailResponseDTO membersDetailResponseDTO=new MembersDetailResponseDTO();
        membersDetailResponseDTO.setId("alex.js");
        membersDetailResponseDTO.setName("alex");
        membersDetailResponseDTO.setDeptName("서비스 개발팀");
        membersDetailResponseDTO.setPoint((long)1000);
        membersDetailResponseDTO.setPenaltyCnt((long)100);
        return membersDetailResponseDTO;

    }


    @Operation(summary = "복지포인트내역 조회", description = "mockup API")
    @GetMapping("/pointhist")
    public PointHistListDTO getMemberpointInfo(){
       PointHistDTO pointHistDTO1=new PointHistDTO((long)100,"macbook pro 응모");
        PointHistDTO pointHistDTO2=new PointHistDTO((long)200,"호텔 숙박권 응모");
       List<PointHistDTO> pointHistDTOS=Arrays.asList(pointHistDTO1,pointHistDTO2);

       return PointHistListDTO.builder()
               .pointHistDTOS(pointHistDTOS)
               .build();
    }

    @Operation(summary = "패널티내역 조회", description = "mockup API")
    @GetMapping("/penaltyhist")
    public PenaltyListResponseDTO getMemberpenaltyInfo(){
        PenaltyResponseDTO penaltyResponseDTO1=new PenaltyResponseDTO("2023-02-01 00:34:13.778134","노쇼",(long)1);
        List<PenaltyResponseDTO> penaltyResponseDTOList=Arrays.asList(penaltyResponseDTO1);

        return PenaltyListResponseDTO.builder()
                .penaltyResponseDTOList(penaltyResponseDTOList)
                .build();
    }

    @Operation(summary = "비밀번호 변경", description = "mockup API")
    @PatchMapping("/update/password")
    public String updatePassword(@RequestBody MemberPasswordDTO memberPasswordDTO){
return "수정되었습니다";
    }

}
