package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.dto.DrawsRequest;
import com._pi.benepick.domain.draws.dto.DrawsResponse;
import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.members.entity.Members;

import java.time.LocalDateTime;

public interface DrawsCommandService {
    DrawsResponse.EditWinnerStatus editWinnerStatus(Members members, Long winnerId, DrawsRequest.DrawsRequestDTO dto);
    void drawStart(LocalDateTime now);
    void updateGoodsStatus(LocalDateTime now);
    DrawsResponse.DrawsResponseResultListDTO verificationSeed(Long goodsId, String seed);
}
