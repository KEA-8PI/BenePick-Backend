package com._pi.benepick.domain.members.service;

import com._pi.benepick.domain.members.dto.MembersRequest.*;
import com._pi.benepick.domain.members.dto.MembersResponse.*;
import com._pi.benepick.domain.members.entity.Members;

public interface MembersCommandService {
    MembersDetailResponseDTO addMembers(AdminMemberRequestDTO membersRequestDTO, Members members);
}
