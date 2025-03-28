package com._pi.benepick.domain.penaltyHists.service;

import com._pi.benepick.domain.penaltyHists.dto.PenaltyRequest.ChangePenaltyHistDTO;
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

    public void createPenaltyHists(ChangePenaltyHistDTO changePenaltyHistDTO) {
        PenaltyHists penaltyHists = PenaltyHists.builder()
                .memberId(changePenaltyHistDTO.getMember())
                .content(changePenaltyHistDTO.getContent())
                .totalPenalty(Math.toIntExact(changePenaltyHistDTO.getTotalPenalty()))
                .penaltyCount(Math.toIntExact(changePenaltyHistDTO.getPenaltyCnt()))
                .build();
        penaltyHistsRepository.save(penaltyHists);
    }
}
