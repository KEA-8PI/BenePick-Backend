package com._pi.benepick.domain.members.service;


import com._pi.benepick.domain.members.dto.MembersResponse.*;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MembersQueryServiceImpl implements MembersQueryService {
    private final MembersRepository membersRepository;

    @Override
    public MembersDetailResponseDTO getMemberinfo(String id){
        Members members=membersRepository.findById(id).orElseThrow(()->new ApiException(ErrorStatus._MEMBERS_NOT_FOUND));
        return MembersDetailResponseDTO.builder()
                .role(members.getRole())
                .point(members.getPoint())
                .name(members.getName())
                .penaltyCnt(members.getPenaltyCnt())
                .deptName(members.getDeptName())
                .id(members.getId())

                .build();
    }

}
