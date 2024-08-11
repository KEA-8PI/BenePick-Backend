package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.dto.DrawsResponse;
import com._pi.benepick.domain.members.entity.Members;
import jakarta.servlet.http.HttpServletResponse;

public interface DrawsQueryService {
    DrawsResponse.DrawsResponseByGoodsListDTO getResultByGoodsId(Long goodsId);
    DrawsResponse.DrawsResponseByWaitlistGoodsIdListDTO getWaitlistByGoodsId(Members members, Long goodsId);
    DrawsResponse.DrawsResponseByWinnerGoodsIdListDTO getWinnersByGoodsId(Members members, Long goodsId);
    DrawsResponse.DrawsResponseByMembersListDTO getCompleteRafflesByMemberId(Members member);
    void downloadExcel(Members members, Long goodsId, HttpServletResponse response);
}
