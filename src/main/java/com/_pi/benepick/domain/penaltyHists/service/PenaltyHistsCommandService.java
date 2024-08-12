package com._pi.benepick.domain.penaltyHists.service;

import com._pi.benepick.domain.penaltyHists.dto.PenaltyRequest.ChangePenaltyHistDTO;

public interface PenaltyHistsCommandService {
    void changePenaltyHist(ChangePenaltyHistDTO changePenaltyHistDTO);
}
