package com._pi.benepick.domain.members.service;

import com._pi.benepick.domain.members.dto.MembersResponse;

public interface MembersQueryService {
    MembersResponse.MembersDetailResponseDTO getMemberinfo(String id);
}
