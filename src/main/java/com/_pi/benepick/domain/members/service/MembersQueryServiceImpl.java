package com._pi.benepick.domain.members.service;

import com._pi.benepick.domain.members.dto.MembersResponse.*;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com._pi.benepick.domain.pointHists.dto.PointResponse.*;
import com._pi.benepick.domain.pointHists.entity.PointHists;
import com._pi.benepick.domain.pointHists.repository.PointHistsRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MembersQueryServiceImpl implements MembersQueryService {
    private final MembersRepository membersRepository;

    private final PointHistsRepository pointHistsRepository;
    @Override
    public MembersDetailListResponseDTO getMembersList(Integer page, Integer size, String keyword){

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Members> membersPage;
        int total=0;
        if(keyword !=null && !keyword.isEmpty()){
            membersPage=membersRepository.findByNameContainingIgnoreCase(keyword, pageRequest);
            total=membersRepository.countAllByName(keyword).size();
        }
        else {
            membersPage=membersRepository.findAll(pageRequest);
            total= (int)membersRepository.count();
        }
        List<MembersDetailResponseDTO> membersDetailResponseDTOList=membersPage.getContent().stream().map(MembersDetailResponseDTO::from).toList();

        return MembersDetailListResponseDTO.builder()
                .membersDetailResponseDTOList(membersDetailResponseDTOList)
                .totalCnt(total)
                .build();

    }


    @Override
        public MembersDetailResponseDTO getMemberinfo(String id) {
            Members members = membersRepository.findById(id).orElseThrow(() -> new ApiException(ErrorStatus._MEMBERS_NOT_FOUND));

            return MembersDetailResponseDTO.from(members);
        }



    //복지 포인트 내역 조회
    @Override
    public PointHistListDTO getPointHist(Integer page,Integer size,Members member) {
        Members members=membersRepository.findById(member.getId()).orElseThrow(()->new ApiException(ErrorStatus._MEMBERS_NOT_FOUND));
        PageRequest pageRequest=PageRequest.of(page,size);
        Page<PointHists> pointHistsPage;
        int total=pointHistsRepository.countAllByMemberId_Id(members.getId());
        pointHistsPage= pointHistsRepository.findAllByMemberId(pageRequest,members.getId());
        List<PointHistDTO> result = pointHistsPage.stream()
                .map(p -> PointHistDTO.builder()
                        .pointChange(p.getPointChange())
                        .totalPoint(p.getTotalPoint())
                        .content(p.getContent())
                        .build())
                .collect(Collectors.toList());
        return PointHistListDTO.builder()
                .pointHistDTOS(result)
                .totalCnt(total)
                .build();
    }

    @Override
    public MemberPointDTO getMemberPoint(Members members){

    Long point =  members.getPoint();

        return MemberPointDTO.builder()
                .point(point)
                .id(members.getId())
                .build();
    }
}
