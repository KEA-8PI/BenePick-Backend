package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.dto.DrawsRequest;
import com._pi.benepick.domain.draws.dto.DrawsResponse;
import com._pi.benepick.domain.members.entity.Members;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;

public interface DrawsComposeService {
    void drawStart(LocalDateTime now);
    DrawsResponse.EditWinnerStatus editWinnerStatus(Members members, Long winnerId, DrawsRequest.DrawsRequestDTO dto);
    DrawsResponse.DrawsResponseResultListDTO verificationSeed(Long goodsId, String seed);DrawsResponse.DrawsResponseByGoodsListDTO getResultByGoodsId(Long goodsId);
    DrawsResponse.DrawsResponseByWaitlistGoodsIdListDTO getWaitlistByGoodsId(Members members, Long goodsId);
    DrawsResponse.DrawsResponseByWinnerGoodsIdListDTO getWinnersByGoodsId(Members members, Long goodsId);
    DrawsResponse.DrawsResponseByMembersListDTO getCompleteRafflesByMemberId(Members member);
    void downloadExcel(Members members, Long goodsId, HttpServletResponse response);
}
