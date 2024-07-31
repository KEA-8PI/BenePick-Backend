package com._pi.benepick.domain.members.service;

import com._pi.benepick.domain.members.dto.MembersResponse;

public interface MembersQueryService {

    MembersResponse.MembersDetailResponseDTO getMemberinfo(String id);

    MembersResponse.MembersDetailListResponseDTO getMembersList(Integer page, Integer size, String keyword);

}
