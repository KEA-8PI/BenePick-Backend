package com._pi.benepick.domain.penaltyHists.service;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.domain.penaltyHists.dto.PenaltyResponse.*;
import com._pi.benepick.domain.penaltyHists.entity.PenaltyHists;
import com._pi.benepick.domain.penaltyHists.repository.PenaltyHistsRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;

import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PenaltyHistsQueryServiceImpl implements PenaltyHistsQueryService
{

    private final PenaltyHistsRepository penaltyHistsRepository;
    private final MembersRepository membersRepository;

    @Override
    public PenaltyListResponseDTO getPenaltyHists(Integer page, Integer size,Members member){
        Members members=membersRepository.findById(member.getId()).orElseThrow(()->new ApiException(ErrorStatus._MEMBERS_NOT_FOUND));

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<PenaltyHists> penaltyHistsPage;
        int total=penaltyHistsRepository.countAllByMemberId_Id(members.getId());
       penaltyHistsPage= penaltyHistsRepository.findAllByMemberId_IdOrderByCreatedAtDesc(pageRequest,members.getId());
        List<PenaltyResponseDTO> result = penaltyHistsPage.stream()
                .map(p -> PenaltyResponseDTO.builder()
                        .penaltyCount(p.getPenaltyCount())
                        .totalPenalty(p.getTotalPenalty())
                        .createdAt(p.getCreatedAt())
                        .content(p.getContent())
                        .build())
                .toList();

       return PenaltyListResponseDTO.builder()
               .penaltyResponseDTOList(result)
               .totalCnt(total)
               .build();
    }
}
