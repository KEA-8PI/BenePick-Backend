package com._pi.benepick.domain.members.service;

import com._pi.benepick.domain.members.dto.MembersRequest;
import com._pi.benepick.domain.members.dto.MembersResponse.*;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.domain.penaltyHists.entity.PenaltyHists;
import com._pi.benepick.domain.penaltyHists.repository.PenaltyHistsRepository;
import com._pi.benepick.domain.pointHists.entity.PointHists;
import com._pi.benepick.domain.pointHists.repository.PointHistsRepository;
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
    private final PointHistsRepository pointHistsRepository;
    private final PenaltyHistsRepository penaltyHistsRepository;
    @Override
    public MembersuccessDTO updateMemberInfo(String memberid, MembersRequest.MembersRequestDTO membersRequestDTO,Members member){
        Members members=membersRepository.findById(memberid).orElseThrow(()->new ApiException(ErrorStatus._MEMBERS_NOT_FOUND));
        if(membersRepository.findById(member.getId()).get().getRole()== Role.MEMBER){
            new ApiException(ErrorStatus._UNAUTHORIZED);
        }
        membersRepository.updateMembers(
                memberid,
                membersRequestDTO.getName(),
                membersRequestDTO.getDeptName(),
                membersRequestDTO.getPoint(),
                membersRequestDTO.getPenaltyCnt()
        );
        changePointHist(membersRequestDTO.getPoint(),memberid,"",members);
        changePenaltyHist(membersRequestDTO.getPenaltyCnt(),memberid," ",members);
        return MembersuccessDTO.builder()
                .msg("수정되었습니다.")
                .build();
    }

    public void changePointHist(Long point,String members,String content,Members member){
        Long totalPoint=pointHistsRepository.findAllByMemberId(members).getTotalPoint();
        Long result=totalPoint+point;
        PointHists pointHists=PointHists.builder()
                .pointChange(point)
                .content(content)
                .totalPoint(result)
                .memberId(member)
                .build();
        pointHistsRepository.save(pointHists);
    }

    public void changePenaltyHist(int penaltycnt,String members,String content,Members member){
        int totalPenalty=penaltyHistsRepository.findAllByMemberId(members).getTotalPenalty();
        int result=totalPenalty+penaltycnt;
        PenaltyHists penaltyHists=PenaltyHists.builder()
                .content(content)
                .memberId(member)
                .penaltyCount(penaltycnt)
                .totalPenalty(result)
                .build();
       penaltyHistsRepository.save(penaltyHists);
    }
}
