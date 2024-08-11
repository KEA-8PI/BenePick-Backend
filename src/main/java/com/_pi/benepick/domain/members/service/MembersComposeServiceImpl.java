package com._pi.benepick.domain.members.service;

import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.members.dto.MembersRequest;
import com._pi.benepick.domain.members.dto.MembersResponse.*;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.domain.penaltyHists.repository.PenaltyHistsRepository;
import com._pi.benepick.domain.penaltyHists.service.PenaltyHistsCommandService;
import com._pi.benepick.domain.pointHists.repository.PointHistsRepository;
import com._pi.benepick.domain.pointHists.service.PointHistsCommandService;
import com._pi.benepick.domain.raffles.repository.RafflesRepository;
import com._pi.benepick.domain.wishlists.repository.WishlistsRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
        Members members=membersQueryService.getMemberById(memberid);
        if(membersQueryService.getMemberRoleByid(member.getId())== Role.MEMBER){
            throw new ApiException(ErrorStatus._UNAUTHORIZED);
        }
        Long totalPoint=membersQueryService.getMemberPoint(memberid);
        Long totalPenalty=membersQueryService.getMemberPenaltyCnt(memberid);

        pointHistsCommandService.changePointHist(membersRequestDTO.getPoint(),"",totalPoint,members);
        penaltyHistsCommandService.changePenaltyHist(membersRequestDTO.getPenaltyCnt(),memberid,"",member,totalPenalty);
        members.updateInfo(membersRequestDTO);

        return UpdateMemberResponseDTO.builder()
                .deptName(membersRequestDTO.getDeptName())
                .name(membersRequestDTO.getName())
                .point(membersRequestDTO.getPoint())
                .penaltyCnt(membersRequestDTO.getPenaltyCnt())
                .role(membersRequestDTO.getRole())
                .build();
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
}
