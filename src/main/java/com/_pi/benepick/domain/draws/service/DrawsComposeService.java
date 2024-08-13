package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.dto.DrawsRequest;
import com._pi.benepick.domain.draws.dto.DrawsResponse.EditWinnerStatus;
import com._pi.benepick.domain.draws.dto.DrawsResponse.DrawsResponseResultListDTO;
import com._pi.benepick.domain.members.entity.Members;

import java.time.LocalDateTime;

public interface DrawsComposeService {
    void drawStart(LocalDateTime now);
    EditWinnerStatus editWinnerStatus(Members members, Long winnerId, DrawsRequest.DrawsRequestDTO dto);
    DrawsResponseResultListDTO verificationSeed(Long goodsId, String seed);
}
