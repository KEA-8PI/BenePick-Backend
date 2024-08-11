package com._pi.benepick.domain.pointHists.service;

import com._pi.benepick.domain.pointHists.dto.PointHistsRequest;
import com._pi.benepick.domain.pointHists.entity.PointHists;
import com._pi.benepick.domain.pointHists.repository.PointHistsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PointHistsCommandServiceImpl implements PointHistsCommandService{

    private final PointHistsRepository pointHistsRepository;
    @Override
    public void changePointHist(PointHistsRequest.changePointHistDTO changePointHistDTO) {
        Long result = changePointHistDTO.getTotalPoint() + changePointHistDTO.getPoint();
        PointHists pointHists = PointHists.builder()
                .pointChange(changePointHistDTO.getPoint())
                .content(changePointHistDTO.getContent())
                .totalPoint(result)
                .memberId(changePointHistDTO.getMembers())
                .build();

        pointHistsRepository.save(pointHists);
    }
}
