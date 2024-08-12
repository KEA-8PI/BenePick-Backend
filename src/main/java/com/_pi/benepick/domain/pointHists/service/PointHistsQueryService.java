package com._pi.benepick.domain.pointHists.service;

import com._pi.benepick.domain.pointHists.entity.PointHists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointHistsQueryService {
    Page<PointHists> getMemberPointHists(Pageable pageable, String id);

    int getMemberPointHistCnt(String id);
}
