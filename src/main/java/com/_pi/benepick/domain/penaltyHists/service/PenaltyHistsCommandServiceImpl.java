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
    @Override
    public void changePenaltyHist(ChangePenaltyHistDTO changePenaltyHistDTO){
        Long result=changePenaltyHistDTO.getTotalPenalty()+changePenaltyHistDTO.getPenaltyCnt();
        PenaltyHists penaltyHists=changePenaltyHistDTO.toEntity(changePenaltyHistDTO,result);
        penaltyHistsRepository.save(penaltyHists);
    }
}
