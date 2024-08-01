package com._pi.benepick.domain.members.service;


import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.domain.pointHists.dto.PointResponse.*;
import com._pi.benepick.domain.pointHists.entity.PointHists;
import com._pi.benepick.domain.pointHists.repository.PointHistsRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.members.dto.MembersResponse.*;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MembersQueryServiceImpl implements MembersQueryService {
    private final MembersRepository membersRepository;

    public MembersDetailResponseDTO getMemberinfo(String id) {
        Members members = membersRepository.findById(id).orElseThrow(() -> new ApiException(ErrorStatus._MEMBERS_NOT_FOUND));
//        return MembersDetailResponseDTO.builder()
//                .point(members.getPoint())
//                .name(members.getName())
//                .penaltyCnt(members.getPenaltyCnt())
//                .deptName(members.getDeptName())
//                .id(members.getId())
//                .build();
        return MembersDetailResponseDTO.from(members);
    }
    private final PointHistsRepository pointHistsRepository;


    //복지 포인트 내역 조회
    @Override
    public PointHistListDTO getPointHist(Members member) {
        Members members = membersRepository.findById(member.getId()).orElseThrow(() -> new ApiException(ErrorStatus._MEMBERS_NOT_FOUND));

        List<PointHists> pointHists = pointHistsRepository.findAllByMemberId(member.getId());
        List<PointHistDTO> pointHistDTOS=pointHists.stream()
                .map(PointHistDTO::from)
                .collect(Collectors.toList());
        return PointHistListDTO.builder()
                .pointHistDTOS(pointHistDTOS)
                .build();
    }

    @Override
    public MembersDetailListResponseDTO getMembersList(Integer page, Integer size, String keyword){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Members> membersPage;
        if(keyword !=null && !keyword.isEmpty()){
            membersPage=membersRepository.findByNameContainingIgnoreCase(keyword, pageRequest);
        }
        else {
            membersPage=membersRepository.findAll(pageRequest);
        }
        List<MembersDetailResponseDTO> membersDetailResponseDTOList=membersPage.getContent().stream().map(MembersDetailResponseDTO::from).collect(Collectors.toList());

        return MembersDetailListResponseDTO.builder()
                .membersDetailResponseDTOList(membersDetailResponseDTOList)
                .build();

    }

}
