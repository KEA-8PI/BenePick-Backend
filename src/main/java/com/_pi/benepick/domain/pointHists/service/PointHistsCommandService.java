package com._pi.benepick.domain.pointHists.service;

import com._pi.benepick.domain.pointHists.dto.PointHistsRequest;

public interface PointHistsCommandService {
    void createPointHists(PointHistsRequest.ChangePointHistDTO changePointRequestDTO);
    void refundPointHists(PointHistsRequest.RefundPointHistDTO refundPointHistDTO);
}
