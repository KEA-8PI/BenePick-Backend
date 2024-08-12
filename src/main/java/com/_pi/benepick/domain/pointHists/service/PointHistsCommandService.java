package com._pi.benepick.domain.pointHists.service;

import com._pi.benepick.domain.pointHists.dto.PointHistsRequest;

public interface PointHistsCommandService {

    void changePointHist(PointHistsRequest.changePointHistDTO changePointHistDTO);
}
