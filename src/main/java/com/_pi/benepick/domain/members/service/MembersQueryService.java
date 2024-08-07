package com._pi.benepick.domain.members.service;


import com._pi.benepick.domain.members.dto.MembersResponse;


import com._pi.benepick.domain.members.dto.MembersResponse;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.pointHists.dto.PointResponse;

public interface MembersQueryService {


    MembersResponse.MembersDetailResponseDTO getMemberinfo(String id);
    MembersResponse.MembersDetailListResponseDTO getMembersList(Integer page, Integer size, String keyword);

    PointResponse.PointHistListDTO getPointHist(Members member);

    MembersResponse.MemberPointDTO getMemberPoint(Members members);
}

