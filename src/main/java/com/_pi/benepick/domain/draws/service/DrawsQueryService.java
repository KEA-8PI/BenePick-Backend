package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.dto.DrawsResponse.DrawsResponseByGoodsListDTO;
import com._pi.benepick.domain.draws.dto.DrawsResponse.DrawsResponseByMembersListDTO;
import com._pi.benepick.domain.draws.dto.DrawsResponse.DrawsResponseByWaitlistGoodsIdListDTO;
import com._pi.benepick.domain.draws.dto.DrawsResponse.DrawsResponseByWinnerGoodsIdListDTO;
import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.draws.entity.Status;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.members.entity.Members;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface DrawsQueryService {
    DrawsResponseByGoodsListDTO getResultByGoodsId(Long goodsId);
    DrawsResponseByWaitlistGoodsIdListDTO getWaitlistByGoodsId(Members members, Long goodsId);
    DrawsResponseByWinnerGoodsIdListDTO getWinnersByGoodsId(Members members, Long goodsId);
    Draws findDrawsById(Long drawsId);
    List<Draws> findAllByGoodsIdAndStatus(Goods goods, Status status);
    DrawsResponseByMembersListDTO getCompleteRafflesByMemberId(Members member);
    void downloadExcel(Members members, Long goodsId, HttpServletResponse response);
    Double getAveragePointByGoodsIdAndStatuses(Long goodsId, List<Status> statuses);
    Long countByRaffleIdsAndStatuses(List<Long> rafflesId, List<Status> statuses);
    List<Draws> getDrawsByGoodsIdAndStatuses(Long goodsId, List<Status> statuses);
}
