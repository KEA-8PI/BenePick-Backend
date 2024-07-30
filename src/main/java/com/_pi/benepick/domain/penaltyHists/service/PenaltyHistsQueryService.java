package com._pi.benepick.domain.penaltyHists.service;

import com._pi.benepick.domain.penaltyHists.dto.PenaltyResponse;

public interface PenaltyHistsQueryService {
    PenaltyResponse.PenaltyListResponseDTO getPenaltyHists(String id);
}
