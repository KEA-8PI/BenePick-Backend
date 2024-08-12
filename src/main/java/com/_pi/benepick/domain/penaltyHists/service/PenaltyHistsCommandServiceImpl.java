package com._pi.benepick.domain.penaltyHists.service;

import com._pi.benepick.domain.penaltyHists.dto.PenaltyRequest.ChangePenaltyHistDTO;
import com._pi.benepick.domain.penaltyHists.entity.PenaltyHists;
import com._pi.benepick.domain.penaltyHists.repository.PenaltyHistsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.penaltyHists.entity.PenaltyHists;
import com._pi.benepick.domain.penaltyHists.repository.PenaltyHistsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PenaltyHistsCommandServiceImpl implements PenaltyHistsCommandService{

    private final PenaltyHistsRepository penaltyHistsRepository;

    public void updatePenaltyHists(Members members, String comment, int penalty) {
        PenaltyHists penaltyHists = PenaltyHists.builder()
                .memberId(members)
                .content(comment)
                .totalPenalty(Math.toIntExact(members.getPenaltyCnt()))
                .penaltyCount(penalty)
                .build();
        penaltyHistsRepository.save(penaltyHists);
    }
}
