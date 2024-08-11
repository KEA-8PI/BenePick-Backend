package com._pi.benepick.domain.penaltyHists.service;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.penaltyHists.dto.PenaltyResponse;

public interface PenaltyHistsQueryService {
    PenaltyResponse.PenaltyListResponseDTO getPenaltyHists(Integer page, Integer size,Members members);
}
