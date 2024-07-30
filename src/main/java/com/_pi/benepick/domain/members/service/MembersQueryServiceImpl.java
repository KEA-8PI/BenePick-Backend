package com._pi.benepick.domain.members.service;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.members.dto.MembersResponse.*;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MembersQueryServiceImpl implements MembersQueryService {
    private final MembersRepository membersRepository;

    @Override
    public MembersDetailListResponseDTO getMembersList(Integer page, Integer size, String keyword){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Members> membersPage;
        if(keyword !=null && !keyword.isEmpty()){
            membersPage=membersRepository.findByNameContainingIgnoreCase(keyword, pageRequest);
        }
        else {
            membersPage=membersRepository.findAll(pageRequest);
        }
        List<MembersDetailResponseDTO> membersDetailResponseDTOList=membersPage.getContent().stream().map(MembersDetailResponseDTO::from).collect(Collectors.toList());

        return MembersDetailListResponseDTO.builder()
                .membersDetailResponseDTOList(membersDetailResponseDTOList)
                .build();
    }

}
