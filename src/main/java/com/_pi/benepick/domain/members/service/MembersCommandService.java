package com._pi.benepick.domain.members.service;
import com._pi.benepick.domain.members.dto.MembersRequest;
import com._pi.benepick.domain.members.dto.MembersResponse;
import com._pi.benepick.domain.members.entity.Members;

public interface MembersCommandService {

    MembersResponse.MembersuccessDTO updateMemberInfo(String memberid, MembersRequest.MembersRequestDTO membersRequestDTO, Members members);


    MembersResponse.MembersuccessDTO changePassword(MembersRequest.MemberPasswordDTO memberPasswordDTO, Members members);

    MembersResponse.MembersDetailResponseDTO addMembers(MembersRequest.AdminMemberRequestDTO membersRequestDTO, Members members);

    MembersResponse.DeleteResponseDTO deleteMembers(MembersRequest.DeleteMembersRequestDTO deleteMembersRequestDTO, Members members);
}
