package com._pi.benepick.domain.penaltyHists.service;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.penaltyHists.dto.PenaltyRequest;

public interface PenaltyHistsCommandService {
    void changePenaltyHist(PenaltyRequest.changePenaltyHistDTO changePenaltyHistDTO);
}
