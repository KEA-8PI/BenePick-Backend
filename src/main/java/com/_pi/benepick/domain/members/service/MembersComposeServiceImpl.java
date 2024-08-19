package com._pi.benepick.domain.members.service;

import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.members.dto.MembersRequest;
import com._pi.benepick.domain.members.dto.MembersResponse;
import com._pi.benepick.domain.members.dto.MembersResponse.*;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.domain.penaltyHists.dto.PenaltyRequest.ChangePenaltyHistDTO;
import com._pi.benepick.domain.penaltyHists.repository.PenaltyHistsRepository;
import com._pi.benepick.domain.penaltyHists.service.PenaltyHistsCommandService;
import com._pi.benepick.domain.pointHists.dto.PointHistsRequest.ChangePointHistDTO;
import com._pi.benepick.domain.pointHists.repository.PointHistsRepository;
import com._pi.benepick.domain.pointHists.service.PointHistsCommandService;
import com._pi.benepick.domain.raffles.repository.RafflesRepository;
import com._pi.benepick.domain.wishlists.repository.WishlistsRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class MembersComposeServiceImpl implements MembersComposeService{

    private final MembersQueryService membersQueryService;
    private final PenaltyHistsCommandService penaltyHistsCommandService;
    private final PointHistsCommandService pointHistsCommandService;
    private final MembersRepository membersRepository;
    private final PenaltyHistsRepository penaltyHistsRepository;
    private final PointHistsRepository pointHistsRepository;
    private final WishlistsRepository wishlistsRepository;
    private final RafflesRepository rafflesRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UpdateMemberResponseDTO updateMemberInfo(String memberid, MembersRequest.MembersRequestDTO membersRequestDTO, Members member){
        if(member.getRole()== Role.MEMBER){
            throw new ApiException(ErrorStatus._UNAUTHORIZED);
        }
        Members updateMember=membersQueryService.getMemberById(memberid);

        Long totalPoint=membersQueryService.getMembertotalPoint(updateMember);
        Long totalPenalty=membersQueryService.getMemberPenaltyCnt(updateMember);

        if(!Objects.equals(totalPoint, membersRequestDTO.getPoint()) ){
            ChangePointHistDTO changePointRequestDTO = new ChangePointHistDTO(
                    membersRequestDTO.getPoint()-totalPoint, "관리자가 변경", membersRequestDTO.getPoint(), updateMember
            );

            pointHistsCommandService.createPointHists(changePointRequestDTO);
        }

        if(!Objects.equals(totalPenalty, membersRequestDTO.getPenaltyCnt()) ){
            ChangePenaltyHistDTO changePenaltyHistDTO= new ChangePenaltyHistDTO(
                    membersRequestDTO.getPenaltyCnt()-totalPenalty,"관리자가 변경", updateMember, membersRequestDTO.getPenaltyCnt()
            );
            penaltyHistsCommandService.createPenaltyHists(changePenaltyHistDTO);
        }
        updateMember.updateInfo(membersRequestDTO);

        return UpdateMemberResponseDTO.builder()
                .deptName(membersRequestDTO.getDeptName())
                .name(membersRequestDTO.getName())
                .point(membersRequestDTO.getPoint())
                .penaltyCnt(membersRequestDTO.getPenaltyCnt())
                .role(membersRequestDTO.getRole())
                .build();
    }

    @Override
    public MembersDetailResponseDTO addMembers(MembersRequest.AdminMemberRequestDTO membersRequestDTO, Members member){
        if(membersRepository.findById(membersRequestDTO.getId()).isPresent()){
            throw new ApiException(ErrorStatus._ALREADY_EXIST_MEMBER);
        }
        if(member.getRole() == Role.MEMBER){
            throw new ApiException(ErrorStatus._UNAUTHORIZED);
        }
        Members members=membersRequestDTO.toEntity(membersRequestDTO);
        members.initPassword(passwordEncoder);
        membersRepository.save(members);
        ChangePointHistDTO changePointRequestDTO = new ChangePointHistDTO(
                0L, "사원 등록", membersRequestDTO.getPoint(), members
        );

        ChangePenaltyHistDTO changePenaltyHistDTO= new ChangePenaltyHistDTO(
                0L,"사원 등록",members,membersRequestDTO.getPenaltyCnt()
        );

        pointHistsCommandService.createPointHists(changePointRequestDTO);
        penaltyHistsCommandService.createPenaltyHists(changePenaltyHistDTO);
        return MembersDetailResponseDTO.from(members);
    }

    @Override
    public DeleteResponseDTO deleteMembers(List<String> memberIdList, Members members){
        //관리자 인지 확인하는 로직
        if(membersQueryService.getMemberRoleByid(members.getId())== Role.MEMBER){
            throw new ApiException(ErrorStatus._UNAUTHORIZED);
        }
        List<String> deletedId = new ArrayList<>();

        for(String id:memberIdList){
            membersRepository.findById(id).orElseThrow(()->new ApiException(ErrorStatus._MEMBERS_NOT_FOUND));
            penaltyHistsRepository.deleteAllByMemberId_Id(id);
            pointHistsRepository.deleteAllByMemberId_Id(id);
            wishlistsRepository.deleteAllByMemberId_Id(id);
            rafflesRepository.deleteAllByMemberId_IdAndGoodsId_GoodsStatus(id, GoodsStatus.PROGRESS);
            membersRepository.deleteById(id);
            deletedId.add(id);
        }

        return DeleteResponseDTO.builder()
                .memberid(deletedId)
                .build();
    }

    @Override
    public MembersDetailListResponseDTO uploadPointFile(MultipartFile file) {
        List<MembersDetailResponseDTO> updatedMembersList = new ArrayList<>();

        int total;
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);

            Row headerRow = sheet.getRow(0);
            if (headerRow == null || !headerRow.getCell(1).getStringCellValue().equals("복지포인트 증감")) {
                throw new ApiException(ErrorStatus._INVALID_FILE_FORMAT);
            }

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                String id = row.getCell(0).getStringCellValue();
                Long pointChange = (long) row.getCell(1).getNumericCellValue();

                Members member = membersRepository.findById(id).orElseThrow(() -> new ApiException(ErrorStatus._MEMBERS_NOT_FOUND));
                pointHistsCommandService.createPointHists(ChangePointHistDTO.builder()
                        .point(pointChange)
                        .content("관리자 수정")
                        .totalPoint(member.getPoint())
                        .members(member)
                        .build());
                member.increasePoint(pointChange);

                updatedMembersList.add(MembersDetailResponseDTO.from(member));
            }
            total = updatedMembersList.size();
        } catch (IOException e) {
            throw new ApiException(ErrorStatus._FILE_INPUT_DISABLED);
        }
        return MembersDetailListResponseDTO.builder()
                .membersDetailResponseDTOList(updatedMembersList)
                .totalCnt(total)
                .build();
    }

    @Override
    public MembersDetailListResponseDTO uploadMemberFile(MultipartFile file) {
        List<Members> membersList = new ArrayList<>();
        int total;
        try (InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);

            Row headerRow = sheet.getRow(0);
            if (headerRow == null || !headerRow.getCell(1).getStringCellValue().equals("이름")) {
                throw new ApiException(ErrorStatus._INVALID_FILE_FORMAT);
            }
            for (Row row : sheet) {
                if (row.getRowNum() == 0) { continue;}
                String id = row.getCell(0).getStringCellValue();
                if(membersRepository.findByIdWithNativeQuery(id).isPresent()){
                    throw new ApiException(ErrorStatus._ALREADY_EXIST_MEMBER);
                }
                Members members = Members.builder()
                    .id(id)
                    .name(row.getCell(1).getStringCellValue())
                    .deptName(row.getCell(2).getStringCellValue())
                    .penaltyCnt((long) row.getCell(3).getNumericCellValue())
                    .point((long) row.getCell(4).getNumericCellValue())
                    .role(Role.MEMBER)
                    .build();
                members.initPassword(passwordEncoder);
                membersList.add(members);
            }
            membersRepository.saveAll(membersList);
            for (Members member : membersList) {
                pointHistsCommandService.createPointHists(ChangePointHistDTO.builder()
                        .point(member.getPoint())
                        .content("사원 등록")
                        .totalPoint(member.getPoint())
                        .members(member)
                    .build());
                penaltyHistsCommandService.createPenaltyHists(ChangePenaltyHistDTO.builder()
                        .totalPenalty(member.getPenaltyCnt())
                        .content("사원 등록")
                        .member(member)
                        .penaltyCnt(member.getPenaltyCnt())
                    .build());
            }
            total = membersList.size();
        } catch (IOException e) {
            throw new ApiException(ErrorStatus._FILE_INPUT_DISABLED);
        }
        List<MembersDetailResponseDTO> responseDTOList = membersList.stream()
            .map(MembersDetailResponseDTO::from)
            .toList();

        return MembersResponse.MembersDetailListResponseDTO.builder()
                .membersDetailResponseDTOList(responseDTOList)
                .totalCnt(total)
                .build();
    }
}
