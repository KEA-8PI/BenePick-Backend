package com._pi.benepick.domain.members.service;
import com._pi.benepick.domain.members.dto.MembersRequest.*;
import com._pi.benepick.domain.members.dto.MembersResponse;
import com._pi.benepick.domain.members.dto.MembersResponse.*;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import com._pi.benepick.domain.members.entity.Role;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;;

@Service
@RequiredArgsConstructor
@Transactional
public class MembersCommandServiceImpl implements MembersCommandService{

    private final MembersRepository membersRepository;

    @Override
    public MembersuccessDTO changePassword(MemberPasswordDTO memberPasswordDTO, Members members){
        if (members.getPassword().equals(memberPasswordDTO.getPassword())){
            throw new ApiException(ErrorStatus._PASSWORD_ALREADY_EXISTS);
        }
        if(!isValid(memberPasswordDTO.getPassword())){
            throw new ApiException(ErrorStatus._PASSWORD_DISABLED);
        }
        members.updatePassword( memberPasswordDTO.getPassword());
        return MembersuccessDTO.builder()
                .msg("성공입니다.")
                .build();
    }

    public boolean isValid(String password) {
        if (password == null) {
            return false;
        }
        String passwordRegex = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}$";
        return password.matches(passwordRegex);
    }

    public MembersDetailResponseDTO addMembers(AdminMemberRequestDTO membersRequestDTO,Members member){
        if(membersRepository.findById(membersRequestDTO.getId()).isPresent()){
            throw new ApiException(ErrorStatus._ALREADY_EXIST_MEMBER);
        }

        if(member.getRole() == Role.MEMBER){
            throw new ApiException(ErrorStatus._UNAUTHORIZED);
        }

        Members members=membersRequestDTO.toEntity(membersRequestDTO);
        membersRepository.save(members);

        return MembersDetailResponseDTO.from(members);
    }
}
