package com._pi.benepick.domain.penaltyHists.service;

import com._pi.benepick.domain.members.entity.Members;

public interface PenaltyHistsCommandService {
    void changePenaltyHist(Long penaltycnt, String members, String content, Members member, Long totalPenalty);
}
