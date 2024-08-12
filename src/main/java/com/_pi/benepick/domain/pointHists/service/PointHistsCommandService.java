package com._pi.benepick.domain.pointHists.service;

import com._pi.benepick.domain.pointHists.dto.PointHistsRequest.ChangePointHistDTO;

public interface PointHistsCommandService {

    void changePointHist(ChangePointHistDTO changePointHistDTO);
}
