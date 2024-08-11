package com._pi.benepick.domain.penaltyHists.service;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.penaltyHists.entity.PenaltyHists;

public interface PenaltyHistsCommandService {
    PenaltyHists updatePenaltyHists(Members members, String comment, int penalty);
}
