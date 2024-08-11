package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.dto.DrawsRequest;
import com._pi.benepick.domain.draws.dto.DrawsResponse;
import com._pi.benepick.domain.members.entity.Members;

import java.time.LocalDateTime;

public interface DrawsCommandService {
    DrawsResponse.EditWinnerStatus editWinnerStatus(Members members, Long winnerId, DrawsRequest.DrawsRequestDTO dto);
    DrawsResponse.DrawsResponseResultListDTO verificationSeed(Long goodsId, String seed);
}
