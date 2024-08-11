package com._pi.benepick.domain.members.service;
import com._pi.benepick.domain.categories.entity.Categories;
import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goodsCategories.entity.GoodsCategories;
import com._pi.benepick.domain.members.dto.MembersRequest;

import com._pi.benepick.domain.draws.repository.DrawsRepository;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.members.dto.MembersRequest.*;

import com._pi.benepick.domain.members.dto.MembersResponse;
import com._pi.benepick.domain.members.dto.MembersResponse.*;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.repository.MembersRepository;

import com._pi.benepick.domain.penaltyHists.repository.PenaltyHistsRepository;
import com._pi.benepick.domain.pointHists.repository.PointHistsRepository;
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.domain.raffles.repository.RafflesRepository;
import com._pi.benepick.domain.wishlists.repository.WishlistsRepository;


import com._pi.benepick.domain.penaltyHists.entity.PenaltyHists;
import com._pi.benepick.domain.penaltyHists.repository.PenaltyHistsRepository;
import com._pi.benepick.domain.pointHists.entity.PointHists;

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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



import java.util.ArrayList;
import java.util.List;




import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MembersCommandServiceImpl implements MembersCommandService{



    private final MembersRepository membersRepository;

    private final PenaltyHistsRepository penaltyHistsRepository;
    private final PointHistsRepository pointHistsRepository;
    private final WishlistsRepository wishlistsRepository;
    private final RafflesRepository rafflesRepository;
    private final DrawsRepository drawsRepository;

    @Override
    public updateMemberResponseDTO updateMemberInfo(String memberid, MembersRequest.MembersRequestDTO membersRequestDTO,Members member){
        Members members=membersRepository.findById(memberid).orElseThrow(()->new ApiException(ErrorStatus._MEMBERS_NOT_FOUND));
        if(membersRepository.findById(member.getId()).get().getRole()== Role.MEMBER){
            new ApiException(ErrorStatus._UNAUTHORIZED);
        }
        changePointHist(membersRequestDTO.getPoint(),memberid,"",members);
        changePenaltyHist(membersRequestDTO.getPenaltyCnt(),memberid," ",members);

        members.updateInfo(membersRequestDTO);

        return updateMemberResponseDTO.builder()
                .deptName(membersRequestDTO.getDeptName())
                .name(membersRequestDTO.getName())
                .point(membersRequestDTO.getPoint())
                .penaltyCnt(membersRequestDTO.getPenaltyCnt())
                .role(membersRequestDTO.getRole())
                .build();
    }

    private void changePointHist(Long point,String members,String content,Members member){
        Long totalPoint=membersRepository.findById(members).get().getPoint();
        Long result=totalPoint+point;

        PointHists pointHists=PointHists.builder()
                .pointChange(point)
                .content(content)
                .totalPoint(result)
                .memberId(member)
                .build();
        pointHistsRepository.save(pointHists);
    }

    private void changePenaltyHist(Long penaltycnt,String members,String content,Members member){
        Long totalPenalty=membersRepository.findById(members).get().getPenaltyCnt();
        Long result=totalPenalty+penaltycnt;
        PenaltyHists penaltyHists=PenaltyHists.builder()
                .content(content)
                .memberId(member)
                .penaltyCount(penaltycnt.intValue())
                .totalPenalty(result.intValue())
                .build();
       penaltyHistsRepository.save(penaltyHists);
    }


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
                    member.increasePoint(pointChange);
                    updatedMembersList.add(MembersDetailResponseDTO.from(member));
                }
            } catch (IOException e) {
                throw new ApiException(ErrorStatus._FILE_INPUT_DISABLED);
            }
            return MembersDetailListResponseDTO.builder()
                    .membersDetailResponseDTOList(updatedMembersList).build();
        }

    @Override
    public DeleteResponseDTO deleteMembers(List<String> memberIdList, Members members){
        //관리자 인지 확인하는 로직
        if(membersRepository.findById(members.getId()).get().getRole()== Role.MEMBER){
            throw new ApiException(ErrorStatus._UNAUTHORIZED);
        }
        List<String> deletedId = new ArrayList<>();

        for(String id:memberIdList){
            Members member = membersRepository.findById(id).orElseThrow(()->new ApiException(ErrorStatus._MEMBERS_NOT_FOUND));
            penaltyHistsRepository.deleteAllByMemberId_Id(id);
            pointHistsRepository.deleteAllByMemberId_Id(id);
            wishlistsRepository.deleteAllByMemberId_Id(id);
          rafflesRepository.deleteAllByMemberId_IdAndGoodsId_GoodsStatus(id,GoodsStatus.PROGRESS);
            membersRepository.deleteById(id);
            deletedId.add(id);
        }
        return DeleteResponseDTO.builder()
                .memberid(deletedId)
                .build();

    }

    // 사원 파일 업로드
    public MembersResponse.MembersDetailListResponseDTO uploadMemberFile(MultipartFile file) {
        List<Members> membersList = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) { continue;}
                String id = row.getCell(0).getStringCellValue();
                Optional<Members> existingMember = membersRepository.findById(id);
                if (existingMember.isEmpty()) {
                    Members members = Members.builder()
                            .id(id)
                            .name(row.getCell(1).getStringCellValue())
                            .deptName(row.getCell(2).getStringCellValue())
                            .password(row.getCell(3).getStringCellValue())
                            .penaltyCnt((long) row.getCell(4).getNumericCellValue())
                            .point((long) row.getCell(5).getNumericCellValue())
                            .role(Role.MEMBER)
                            .build();
                    membersList.add(members);
                }
            }
            membersRepository.saveAll(membersList);
        } catch (IOException e) {
            throw new ApiException(ErrorStatus._FILE_INPUT_DISABLED);
        }
        List<MembersDetailResponseDTO> responseDTOList = membersList.stream()
                .map(MembersDetailResponseDTO::from)
                .collect(Collectors.toList());

        return MembersResponse.MembersDetailListResponseDTO.builder()
                .membersDetailResponseDTOList(responseDTOList)
                .build();
    }

}
