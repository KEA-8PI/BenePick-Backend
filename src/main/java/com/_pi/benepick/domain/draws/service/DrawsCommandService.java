package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.dto.DrawsRequest;
import com._pi.benepick.domain.draws.dto.DrawsResponse;
import com._pi.benepick.domain.members.entity.Members;

public interface DrawsCommandService {
    DrawsResponse.DrawsResponseByMembersDTO editWinnerStatus(Members members, Long winnerId, DrawsRequest.DrawsRequestDTO dto);
    void drawStart(Long goodsId);
    DrawsResponse.DrawsResponseResultListDTO verificationSeed(Long goodsId, String seed);
}
