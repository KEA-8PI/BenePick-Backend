package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.dto.DrawsRequest;
import com._pi.benepick.domain.draws.dto.DrawsResponse;

public interface DrawsQueryService {
    DrawsResponse.DrawsResponseByGoodsListDTO getWaitlistByGoodsId(Long goodsId);
    DrawsResponse.DrawsResponseByGoodsListDTO getWinnersByGoodsId(Long goodsId);
    DrawsResponse.DrawsResponseByMembersListDTO getCompleteRafflesByMemberId(String memberId);
    DrawsResponse.DrawsResponseByMembersDTO editWinnerStatus(Long winnerId, DrawsRequest.DrawsRequestDTO dto);
}
