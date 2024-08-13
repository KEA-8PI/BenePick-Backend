package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.dto.DrawsResponse;
import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.draws.entity.Status;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.raffles.entity.Raffles;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface DrawsQueryService {
    DrawsResponse.DrawsResponseByGoodsListDTO getResultByGoodsId(Long goodsId);
    DrawsResponse.DrawsResponseByWaitlistGoodsIdListDTO getWaitlistByGoodsId(Members members, Long goodsId);
    DrawsResponse.DrawsResponseByWinnerGoodsIdListDTO getWinnersByGoodsId(Members members, Long goodsId);
    Draws findDrawsById(Long drawsId);
    List<Draws> findAllByGoodsIdAndStatus(Goods goods, Status status);
    List<Draws> findByMemberId(Members member);
    void downloadExcel(Members members, Long goodsId, HttpServletResponse response);
    Double getAveragePointByGoodsIdAndStatus(Long goodsId, Status status);
    Long countByRaffleIdsAndStatuses(List<Long> rafflesId, List<Status> statuses);
    List<Draws> getDrawsByGoodsIdAndStatuses(Long goodsId, List<Status> statuses);
}
