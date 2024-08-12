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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

    @Override
    public UpdateMemberResponseDTO updateMemberInfo(String memberid, MembersRequest.MembersRequestDTO membersRequestDTO, Members member){
        if(member.getRole()== Role.MEMBER){
            throw new ApiException(ErrorStatus._UNAUTHORIZED);
        }
        Members updateMember=membersQueryService.getMemberById(memberid);

        Long totalPoint=membersQueryService.getMembertotalPoint(updateMember);
        Long totalPenalty=membersQueryService.getMemberPenaltyCnt(updateMember);

        ChangePointHistDTO changePointRequestDTO = new ChangePointHistDTO(
                membersRequestDTO.getPoint(), "관리자가 변경", totalPoint, updateMember
        );

        ChangePenaltyHistDTO changePenaltyHistDTO= new ChangePenaltyHistDTO(
                membersRequestDTO.getPenaltyCnt(),"관리자가 변경",updateMember,totalPenalty
        );

        pointHistsCommandService.changePointHist(changePointRequestDTO);
        penaltyHistsCommandService.changePenaltyHist(changePenaltyHistDTO);
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
        membersRepository.save(members);

        ChangePointHistDTO changePointRequestDTO = new ChangePointHistDTO(
                0L, "사원 등록", membersRequestDTO.getPoint(), members
        );

        ChangePenaltyHistDTO changePenaltyHistDTO= new ChangePenaltyHistDTO(
                0L,"사원 등록",members,membersRequestDTO.getPenaltyCnt()
        );

        pointHistsCommandService.changePointHist(changePointRequestDTO);
        penaltyHistsCommandService.changePenaltyHist(changePenaltyHistDTO);
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

        try (InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream)) {

            XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) { continue;}

                String id = row.getCell(0).getStringCellValue();
                Long pointChange = (long)row.getCell(1).getNumericCellValue();

                Members member = membersRepository.findById(id).orElseThrow(() -> new ApiException(ErrorStatus._MEMBERS_NOT_FOUND));
                pointHistsCommandService.changePointHist(ChangePointHistDTO.builder()
                    .point(pointChange)
                    .content("관리자 수정")
                    .totalPoint(member.getPoint())
                    .members(member)
                    .build());
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
    public MembersDetailListResponseDTO uploadMemberFile(MultipartFile file) {
        List<Members> membersList = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) { continue;}
                String id = row.getCell(0).getStringCellValue();
                membersRepository.findByIdWithNativeQuery(id).orElseThrow(()->new ApiException(ErrorStatus._ALREADY_EXIST_MEMBER));
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
            membersRepository.saveAll(membersList);
            for (Members member : membersList) {
                pointHistsCommandService.changePointHist(ChangePointHistDTO.builder()
                        .point(member.getPoint())
                        .content("사원 등록")
                        .totalPoint(member.getPoint())
                        .members(member)
                    .build());
                penaltyHistsCommandService.changePenaltyHist(ChangePenaltyHistDTO.builder()
                        .totalPenalty(member.getPenaltyCnt())
                        .content("사원 등록")
                        .member(member)
                        .penaltyCnt(member.getPenaltyCnt())
                    .build());
            }
        } catch (IOException e) {
            throw new ApiException(ErrorStatus._FILE_INPUT_DISABLED);
        }
        List<MembersDetailResponseDTO> responseDTOList = membersList.stream()
            .map(MembersDetailResponseDTO::from)
            .toList();

        return MembersResponse.MembersDetailListResponseDTO.builder()
            .membersDetailResponseDTOList(responseDTOList)
            .build();
    }
}
