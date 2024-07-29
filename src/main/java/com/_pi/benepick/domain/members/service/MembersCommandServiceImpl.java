package com._pi.benepick.domain.members.service;

import com._pi.benepick.domain.members.dto.MembersRequest.*;
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
@Transactional
public class MembersCommandServiceImpl implements MembersCommandService{
    private final MembersRepository membersRepository;

    @Override
    public MembersDetailResponseDTO addMembers(MembersRequestDTO membersRequestDTO){
        if(membersRepository.findById(membersRequestDTO.getId()).isPresent()){
            throw new ApiException(ErrorStatus._ALREADY_EXIST_MEMBER);
        }

        Members members=Members.builder()
                .id(membersRequestDTO.getId())
                .deptName(membersRequestDTO.getDeptName())
                .name(membersRequestDTO.getName())
                .point(membersRequestDTO.getPoint())
                .penaltyCnt(membersRequestDTO.getPenaltyCnt())
                .role(membersRequestDTO.getRole())
                .password(membersRequestDTO.getPassword())
                .build();
        membersRepository.save(members);

        return MembersDetailResponseDTO.builder()
                .id(membersRequestDTO.getId())
                .deptName(membersRequestDTO.getDeptName())
                .name(membersRequestDTO.getName())
                .point(membersRequestDTO.getPoint())
                .penaltyCnt(membersRequestDTO.getPenaltyCnt())
                .role(membersRequestDTO.getRole())

                .build();
    }


}
