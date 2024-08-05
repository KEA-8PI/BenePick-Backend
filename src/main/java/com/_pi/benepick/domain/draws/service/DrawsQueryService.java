package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.dto.DrawsRequest;
import com._pi.benepick.domain.draws.dto.DrawsResponse;
import com._pi.benepick.domain.members.entity.Members;
import jakarta.servlet.http.HttpServletResponse;

public interface DrawsQueryService {
    DrawsResponse.DrawsResponseByGoodsListDTO getResultByGoodsId(Long goodsId);
    DrawsResponse.DrawsResponseByGoodsListDTO getWaitlistByGoodsId(Members members, Long goodsId);
    DrawsResponse.DrawsResponseByGoodsListDTO getWinnersByGoodsId(Members members, Long goodsId);
    DrawsResponse.DrawsResponseByMembersListDTO getCompleteRafflesByMemberId(String memberId);
    void downloadExcel(Members members, Long goodsId, HttpServletResponse response);
}
