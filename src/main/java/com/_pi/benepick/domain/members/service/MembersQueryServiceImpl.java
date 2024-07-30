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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MembersQueryServiceImpl implements MembersQueryService {
    private final MembersRepository membersRepository;
    private final PointHistsRepository pointHistsRepository;


    //복지 포인트 내역 조회
    @Override
    public PointHistListDTO getPointHist(String id){
        Members members=membersRepository.findById(id).orElseThrow(()->new ApiException(ErrorStatus._MEMBER_NOT_FOUND));
        List<PointHists> pointHists=pointHistsRepository.findAllByMemberId(id);
        List<PointHistDTO> result=new ArrayList<>();
        for(PointHists p:pointHists){
            PointHistDTO point=PointHistDTO.builder()
                    .pointChange(p.getPointChange())
                    .content(p.getContent())
                    .totalPoint(p.getTotalPoint())
                    .content(p.getContent())
                    .build();
            result.add(point);
        }
        return PointHistListDTO.builder()
                .pointHistDTOS(result)
                .build();

    }

}
