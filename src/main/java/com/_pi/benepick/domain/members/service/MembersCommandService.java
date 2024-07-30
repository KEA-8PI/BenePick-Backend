package com._pi.benepick.domain.members.service;

import com._pi.benepick.domain.members.dto.MembersRequest;
import com._pi.benepick.domain.members.dto.MembersResponse;

public interface MembersCommandService {

    MembersResponse.MembersuccessDTO updateMemberInfo(String memberid, MembersRequest.MembersRequestDTO membersRequestDTO);
}
