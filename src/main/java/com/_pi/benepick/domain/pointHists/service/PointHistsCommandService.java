package com._pi.benepick.domain.pointHists.service;

import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.members.entity.Members;

public interface PointHistsCommandService {
    void savePointHists(Members members, String comment, Long point);
    void refundPointHists(Members members, Draws draws, String comment);
}
