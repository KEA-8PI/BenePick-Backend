package com._pi.benepick.domain.pointHists.service;

import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.pointHists.dto.PointHistsRequest;

public interface PointHistsCommandService {
    void savePointHists(PointHistsRequest.ChangePointHistDTO changePointRequestDTO);
    void refundPointHists(PointHistsRequest.RefundPointHistDTO refundPointHistDTO);
}
