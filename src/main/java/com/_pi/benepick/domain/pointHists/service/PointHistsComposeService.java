package com._pi.benepick.domain.pointHists.service;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.pointHists.dto.PointResponse;

public interface PointHistsComposeService {
    PointResponse.PointHistListDTO getPointHist(Integer page, Integer size, Members member);
}
