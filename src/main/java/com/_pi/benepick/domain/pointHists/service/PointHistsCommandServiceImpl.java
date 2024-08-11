package com._pi.benepick.domain.pointHists.service;

import com._pi.benepick.domain.members.entity.Members;
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
    public void changePointHist(Long point, String content, Long totalPoint, Members member) {
        Long result = totalPoint + point;
        PointHists pointHists = PointHists.builder()
                .pointChange(point)
                .content(content)
                .totalPoint(result)
                .memberId(member)
                .build();

        pointHistsRepository.save(pointHists);
    }
}
