package com._pi.benepick.domain.pointHists.service;

import com._pi.benepick.domain.draws.entity.Draws;
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

    public void savePointHists(Members members, String comment, Long point) {
        PointHists pointHists = PointHists.builder()
                .memberId(members)
                .content(comment)
                .pointChange(point)
                .totalPoint(members.getPoint())
                .build();
        pointHistsRepository.save(pointHists);
    }

    public void refundPointHists(Members members, Draws draws, String comment) {
        PointHists pointHists = PointHists.builder()
                .memberId(members)
                .content(comment)
                .pointChange(Math.round(draws.getRaffleId().getPoint() / 2.0))
                .totalPoint(members.getPoint())
                .build();
        pointHistsRepository.save(pointHists);
    }
}
