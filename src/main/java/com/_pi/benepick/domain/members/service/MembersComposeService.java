package com._pi.benepick.domain.members.service;

import com._pi.benepick.domain.members.dto.MembersRequest;
import com._pi.benepick.domain.members.dto.MembersResponse;
import com._pi.benepick.domain.members.dto.MembersResponse.MembersDetailListResponseDTO;
import com._pi.benepick.domain.members.entity.Members;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MembersComposeService {
    MembersResponse.UpdateMemberResponseDTO updateMemberInfo(String memberid, MembersRequest.MembersRequestDTO membersRequestDTO, Members member);

    MembersResponse.MembersDetailResponseDTO addMembers(MembersRequest.AdminMemberRequestDTO membersRequestDTO, Members member);

    MembersResponse.DeleteResponseDTO deleteMembers(List<String> memberIdList, Members members);

    MembersDetailListResponseDTO uploadPointFile(MultipartFile file);
    MembersResponse.MembersDetailListResponseDTO uploadMemberFile(MultipartFile file);
}
