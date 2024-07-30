package com._pi.benepick.domain.members.service;

import com._pi.benepick.domain.pointHists.dto.PointResponse;

public interface MembersQueryService {
    PointResponse.PointHistListDTO getPointHist(String id);
}
