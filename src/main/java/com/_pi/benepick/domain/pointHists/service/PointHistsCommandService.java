package com._pi.benepick.domain.pointHists.service;

import com._pi.benepick.domain.members.entity.Members;

public interface PointHistsCommandService {

    void changePointHist(Long point, String content, Long totalPoint, Members member);
}
