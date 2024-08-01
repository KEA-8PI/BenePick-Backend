package com._pi.benepick.domain.members.service;


import com._pi.benepick.domain.members.dto.MembersResponse;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.pointHists.dto.PointResponse;

public interface MembersQueryService {

    MembersResponse.MembersDetailResponseDTO getMemberinfo(String id);
    MembersResponse.MembersDetailListResponseDTO getMembersList(Integer page, Integer size, String keyword);
    //복지 포인트 내역 조회
    PointResponse.PointHistListDTO getPointHist(Members member);
}