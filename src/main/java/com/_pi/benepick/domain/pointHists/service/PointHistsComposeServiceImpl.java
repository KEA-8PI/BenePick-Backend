package com._pi.benepick.domain.pointHists.service;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.service.MembersQueryService;
import com._pi.benepick.domain.pointHists.dto.PointResponse.*;
import com._pi.benepick.domain.pointHists.entity.PointHists;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PointHistsComposeServiceImpl implements PointHistsComposeService{
    private final MembersQueryService membersQueryService;
private final PointHistsQueryService pointHistsQueryService;
    @Override
    public PointHistListDTO getPointHist(Integer page, Integer size, Members member){
        Members members=membersQueryService.getMemberById(member.getId());
        PageRequest pageRequest=PageRequest.of(page,size);
        Page<PointHists> pointHistsPage;

        int total=pointHistsQueryService.getMemberPointHistCnt(members.getId());

        pointHistsPage=pointHistsQueryService.getMemberPointHists(pageRequest, members.getId());
        List<PointHistDTO> result = pointHistsPage.stream()
                .map(p -> PointHistDTO.builder()
                        .pointChange(p.getPointChange())
                        .totalPoint(p.getTotalPoint())
                        .content(p.getContent())
                        .createdAt(p.getCreatedAt())
                        .build())
                .toList();

        return PointHistListDTO.builder()
                .pointHistDTOS(result)
                .totalCnt(total)
                .build();
    }

}
