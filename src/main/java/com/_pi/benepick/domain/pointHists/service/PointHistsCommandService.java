package com._pi.benepick.domain.pointHists.service;

import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.pointHists.entity.PointHists;

public interface PointHistsCommandService {
    PointHists savePointHists(Members members, String comment, Long point);
    PointHists refundPointHists(Members members, Draws draws, String comment);
}
