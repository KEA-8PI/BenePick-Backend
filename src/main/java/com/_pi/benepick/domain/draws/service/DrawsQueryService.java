package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.dto.DrawsRequest;
import com._pi.benepick.domain.draws.dto.DrawsResponse;
import com._pi.benepick.domain.members.entity.Members;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface DrawsQueryService {
    DrawsResponse.DrawsResponseByGoodsListDTO getWaitlistByGoodsId(Long goodsId);
    DrawsResponse.DrawsResponseByGoodsListDTO getWinnersByGoodsId(Long goodsId);
    DrawsResponse.DrawsResponseByMembersListDTO getCompleteRafflesByMemberId(String memberId);
    DrawsResponse.DrawsResponseByMembersDTO editWinnerStatus(Members members, Long winnerId, DrawsRequest.DrawsRequestDTO dto);
    void downloadExcel(Members members, Long goodsId, HttpServletResponse response);
}
