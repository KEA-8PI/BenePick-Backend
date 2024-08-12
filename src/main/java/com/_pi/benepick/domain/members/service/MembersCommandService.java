package com._pi.benepick.domain.members.service;
import com._pi.benepick.domain.members.dto.MembersRequest;
import com._pi.benepick.domain.members.dto.MembersResponse;
import com._pi.benepick.domain.members.entity.Members;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface MembersCommandService {

    MembersResponse.MembersuccessDTO changePassword(MembersRequest.MemberPasswordDTO memberPasswordDTO, Members members);
    MembersResponse.MembersDetailResponseDTO addMembers(MembersRequest.AdminMemberRequestDTO membersRequestDTO, Members members);
}
