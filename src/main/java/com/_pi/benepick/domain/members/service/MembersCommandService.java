package com._pi.benepick.domain.members.service;
import com._pi.benepick.domain.members.dto.MembersRequest;
import com._pi.benepick.domain.members.dto.MembersResponse;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.global.common.response.ApiResponse;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public interface MembersCommandService {

    MembersResponse.MembersuccessDTO updateMemberInfo(String memberid, MembersRequest.MembersRequestDTO membersRequestDTO, Members members);


    MembersResponse.MembersuccessDTO changePassword(MembersRequest.MemberPasswordDTO memberPasswordDTO, Members members);

    MembersResponse.MembersDetailResponseDTO addMembers(MembersRequest.AdminMemberRequestDTO membersRequestDTO, Members members);

    MembersResponse.MembersDetailListResponseDTO uploadMemberFile(MultipartFile file); // 사원 정보 파일 업로드
}