package com._pi.benepick.domain.members.service;
import com._pi.benepick.domain.members.dto.MembersRequest.*;
import com._pi.benepick.domain.members.dto.MembersResponse.*;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
@RequiredArgsConstructor
@Transactional
public class MembersCommandServiceImpl implements MembersCommandService{

    private final PasswordEncoder passwordEncoder;
    @Override
    public MembersuccessDTO changePassword(MemberPasswordDTO memberPasswordDTO, Members members){
        //기존 비밀번호 확인
        if(!passwordEncoder.matches(memberPasswordDTO.getPassword(),members.getPassword())){
            throw new ApiException(ErrorStatus._MEMBER_PASSWORD_NOT_MATCH);
        }
        //새로운 비밀번호랑 기존 비밀번호가 같은지 확인
        if(passwordEncoder.matches(memberPasswordDTO.getNewPassword(),members.getPassword())){
            throw new ApiException(ErrorStatus._PASSWORD_ALREADY_EXISTS);
        }
        members.updatePassword(memberPasswordDTO.getNewPassword(),passwordEncoder);
        return MembersuccessDTO.builder()
                .msg("성공입니다.")
                .build();
    }

}
