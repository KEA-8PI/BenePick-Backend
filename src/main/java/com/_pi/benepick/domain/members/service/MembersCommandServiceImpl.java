package com._pi.benepick.domain.members.service;

import com._pi.benepick.domain.members.dto.MembersRequest.*;
import com._pi.benepick.domain.members.dto.MembersResponse.*;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import com._pi.benepick.domain.members.entity.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
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

        if(membersRepository.findById(member.getId()).get().getRole()== Role.MEMBER){
            throw new ApiException(ErrorStatus._UNAUTHORIZED);
        }

        Members members=membersRequestDTO.toEntity(membersRequestDTO);
        membersRepository.save(members);

        return MembersDetailResponseDTO.from(members);

    }

    // 복지포인트 파일 업로드
    @Override
    public MembersDetailListResponseDTO uploadPointFile(MultipartFile file) {
        List<MembersDetailResponseDTO> updatedMembersList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) { continue;}

                String id = row.getCell(0).getStringCellValue();
                Long pointChange = (long)row.getCell(1).getNumericCellValue();

                Members member = membersRepository.findById(id).orElseThrow(() -> new ApiException(ErrorStatus._MEMBERS_NOT_FOUND));
                member.updatePoint(member.getPoint() + pointChange);
                updatedMembersList.add(MembersDetailResponseDTO.from(member));
            }
        } catch (IOException e) {
            throw new ApiException(ErrorStatus._FILE_INPUT_DISABLED);
        }
        return MembersDetailListResponseDTO.builder()
                .membersDetailResponseDTOList(updatedMembersList).build();
    }

}
