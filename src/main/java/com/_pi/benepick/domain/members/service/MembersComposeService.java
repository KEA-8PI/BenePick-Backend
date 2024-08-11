package com._pi.benepick.domain.members.service;

import com._pi.benepick.domain.members.dto.MembersRequest;
import com._pi.benepick.domain.members.dto.MembersResponse;
import com._pi.benepick.domain.members.entity.Members;

import java.util.List;

public interface MembersComposeService {
    MembersResponse.UpdateMemberResponseDTO updateMemberInfo(String memberid, MembersRequest.MembersRequestDTO membersRequestDTO, Members member);

    MembersResponse.DeleteResponseDTO deleteMembers(List<String> memberIdList, Members members);
}
