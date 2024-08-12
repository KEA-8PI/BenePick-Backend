package com._pi.benepick.domain.pointHists.service;

import com._pi.benepick.domain.pointHists.dto.PointHistsRequest.ChangePointHistDTO;
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
    public void changePointHist(ChangePointHistDTO changePointHistDTO) {
        Long result = changePointHistDTO.getTotalPoint() + changePointHistDTO.getPoint();
        PointHists pointHists = changePointHistDTO.toEntity(changePointHistDTO,result);
        pointHistsRepository.save(pointHists);
    }
}
