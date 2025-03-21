package com._pi.benepick.domain.members.service;
import com._pi.benepick.domain.members.dto.MembersResponse;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;

public interface MembersQueryService {

    MembersResponse.MembersDetailResponseDTO getMemberinfo(Members members);
    MembersResponse.MembersDetailListResponseDTO getMembersList(Integer page, Integer size, String keyword);

    MembersResponse.MemberPointDTO getMemberPoint(Members members);

    Members getMemberById(String id);

    Role getMemberRoleByid(String id);

    Long getMembertotalPoint(Members members);

    Long getMemberPenaltyCnt(Members members);
}

