package com._pi.benepick.domain.members.service;

import com._pi.benepick.domain.members.dto.MembersRequest;
import com._pi.benepick.domain.members.dto.MembersResponse.*;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.domain.penaltyHists.repository.PenaltyHistsRepository;
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
    public MembersuccessDTO updateMemberInfo(String memberid, MembersRequest.MembersRequestDTO membersRequestDTO){
        Members members=membersRepository.findById(memberid).orElseThrow(()->new ApiException(ErrorStatus._MEMBERS_NOT_FOUND));
        membersRepository.updateMembers(
                memberid,
                membersRequestDTO.getName(),
                membersRequestDTO.getDeptName(),
                membersRequestDTO.getPoint(),
                membersRequestDTO.getPenaltyCnt()
        );
        changePointHist(membersRequestDTO.getPoint(),memberid,"");
        changePenaltyHist(membersRequestDTO.getPenaltyCnt(),memberid," ");
        return MembersuccessDTO.builder()
                .msg("수정되었습니다.")
                .build();
    }

    public void changePointHist(Long point,String id,String content){
        pointHistsRepository.updatePointHist(id,point,content);

    }

    public void changePenaltyHist(int penaltycnt,String id,String content){
        penaltyHistsRepository.updatePenaltyHist(id,penaltycnt,content);
    }
}
