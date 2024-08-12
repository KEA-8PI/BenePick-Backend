package com._pi.benepick.domain.pointHists.service;

import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.pointHists.dto.PointHistsRequest;
import com._pi.benepick.domain.pointHists.entity.PointHists;
import com._pi.benepick.domain.pointHists.repository.PointHistsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PointHistsCommandServiceImpl implements PointHistsCommandService{

    private final PointHistsRepository pointHistsRepository;

    public void createPointHists(PointHistsRequest.ChangePointHistDTO changePointRequestDTO) {
        PointHists pointHists = PointHists.builder()
                .memberId(changePointRequestDTO.getMembers())
                .content(changePointRequestDTO.getContent())
                .pointChange(changePointRequestDTO.getPoint())
                .totalPoint(changePointRequestDTO.getTotalPoint())
                .build();
        pointHistsRepository.save(pointHists);
    }

    public void refundPointHists(PointHistsRequest.RefundPointHistDTO refundPointHistDTO) {
        PointHists pointHists = PointHists.builder()
                .memberId(refundPointHistDTO.getDraws().getRaffleId().getMemberId())
                .content(refundPointHistDTO.getContent())
                .pointChange(Math.round(refundPointHistDTO.getDraws().getRaffleId().getPoint() / 2.0))
                .totalPoint(refundPointHistDTO.getDraws().getRaffleId().getMemberId().getPoint())
                .build();
        pointHistsRepository.save(pointHists);
    }
}
