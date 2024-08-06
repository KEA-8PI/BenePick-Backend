package com._pi.benepick.domain.members.service;
import com._pi.benepick.domain.members.dto.MembersRequest;

import com._pi.benepick.domain.draws.repository.DrawsRepository;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.members.dto.MembersRequest.*;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class MembersCommandServiceImpl implements MembersCommandService{



    private final MembersRepository membersRepository;

    private final PenaltyHistsRepository penaltyHistsRepository;
    private final PointHistsRepository pointHistsRepository;
    private final WishlistsRepository wishlistsRepository;
    private final RafflesRepository rafflesRepository;
    private final DrawsRepository drawsRepository;

    @Override
    public MembersuccessDTO updateMemberInfo(String memberid, MembersRequest.MembersRequestDTO membersRequestDTO,Members member){
        Members members=membersRepository.findById(memberid).orElseThrow(()->new ApiException(ErrorStatus._MEMBERS_NOT_FOUND));
        if(membersRepository.findById(member.getId()).get().getRole()== Role.MEMBER){
            new ApiException(ErrorStatus._UNAUTHORIZED);
        }
        changePointHist(membersRequestDTO.getPoint(),memberid,"",members);
        changePenaltyHist(membersRequestDTO.getPenaltyCnt(),memberid," ",members);

        members.updateInfo(membersRequestDTO);

        return MembersuccessDTO.builder()
                .msg("수정되었습니다.")
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

    @Override
    public DeleteResponseDTO deleteMembers(DeleteMembersRequestDTO deleteMembersRequestDTO, Members members){
        //관리자 인지 확인하는 로직
        if(membersRepository.findById(members.getId()).get().getRole()== Role.MEMBER){
            throw new ApiException(ErrorStatus._UNAUTHORIZED);
        }
        List<String> deletedId = new ArrayList<>();

        for(String id:deleteMembersRequestDTO.getId()){
            Members member = membersRepository.findById(id).orElseThrow(()->new ApiException(ErrorStatus._MEMBERS_NOT_FOUND));
            penaltyHistsRepository.deleteAllByMemberId_Id(id);
            pointHistsRepository.deleteAllByMemberId_Id(id);
            wishlistsRepository.deleteAllByMemberId_Id(id);
           // List<Raffles> rafflesList = rafflesRepository.findAllByMemberId_Id(id);

            // For each raffle, delete associated draws
//            for (Raffles raffle : rafflesList) {
//                drawsRepository.deleteAllByMemberId(raffle.getId());
//            }
          //  rafflesRepository.deleteAllByMemberId(id, GoodsStatus.PROGRESS);
            rafflesRepository.deleteAllByMemberId_IdAndGoodsId_GoodsStatus_Progress(id,GoodsStatus.PROGRESS);
            membersRepository.deleteById(id);
            deletedId.add(id);
        }
        return DeleteResponseDTO.builder()
                .memberid(deletedId)
                .build();

    }


}
