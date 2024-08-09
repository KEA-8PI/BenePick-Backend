package com._pi.benepick.domain.members.service;
import com._pi.benepick.domain.members.dto.MembersRequest;
import com._pi.benepick.domain.members.dto.MembersResponse;
import com._pi.benepick.domain.members.entity.Members;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import com._pi.benepick.global.common.response.ApiResponse;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public interface MembersCommandService {

    MembersResponse.updateMemberResponseDTO updateMemberInfo(String memberid, MembersRequest.MembersRequestDTO membersRequestDTO, Members members);
    MembersResponse.MembersuccessDTO changePassword(MembersRequest.MemberPasswordDTO memberPasswordDTO, Members members);
    MembersResponse.MembersDetailResponseDTO addMembers(MembersRequest.AdminMemberRequestDTO membersRequestDTO, Members members);
    MembersResponse.MembersDetailListResponseDTO uploadPointFile(MultipartFile file) throws IOException; // 복지포인트 파일 업로드
    MembersResponse.DeleteResponseDTO deleteMembers(MembersRequest.DeleteMembersRequestDTO deleteMembersRequestDTO, Members members);
    MembersResponse.MembersDetailListResponseDTO uploadMemberFile(MultipartFile file); // 사원 정보 파일 업로드
}
