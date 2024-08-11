package com._pi.benepick.domain.penaltyHists.service;

import com._pi.benepick.domain.members.entity.Members;

public interface PenaltyHistsCommandService {
    void updatePenaltyHists(Members members, String comment, int penalty);
}
