package com._pi.benepick.domain.penaltyHists.service;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.penaltyHists.dto.PenaltyRequest;
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
    @Override
    public void changePenaltyHist(PenaltyRequest.changePenaltyHistDTO changePenaltyHistDTO){
        Long result=changePenaltyHistDTO.getTotalPenalty()+changePenaltyHistDTO.getPenaltyCnt();
        PenaltyHists penaltyHists=PenaltyHists.builder()
                .content(changePenaltyHistDTO.getContent())
                .memberId(changePenaltyHistDTO.getMember())
                .penaltyCount(changePenaltyHistDTO.getPenaltyCnt().intValue())
                .totalPenalty(result.intValue())
                .build();
        penaltyHistsRepository.save(penaltyHists);
    }
}
