package com._pi.benepick.domain.members.service;
import com._pi.benepick.domain.members.dto.MembersResponse.*;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.domain.members.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MembersQueryServiceImpl implements MembersQueryService {
    private final MembersRepository membersRepository;
    @Override
    public MembersDetailListResponseDTO getMembersList(Integer page, Integer size, String keyword){

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Members> membersPage;
        int total=0;
        if(keyword !=null && !keyword.isEmpty()){
            membersPage=membersRepository.findByNameContainingIgnoreCase(keyword, pageRequest, Role.MEMBER);
           total=(int)membersPage.getTotalElements();
        }
        else {
            membersPage=membersRepository.findAllByRole(Role.MEMBER,pageRequest);
            total=(int)membersPage.getTotalElements();
        }
        List<MembersDetailResponseDTO> membersDetailResponseDTOList=membersPage.getContent().stream().map(MembersDetailResponseDTO::from).toList();

        return MembersDetailListResponseDTO.builder()
                .membersDetailResponseDTOList(membersDetailResponseDTOList)
                .totalCnt(total)
                .build();
    }

    @Override
        public MembersDetailResponseDTO getMemberinfo(Members member) {
            Members members = membersRepository.findById(member.getId()).orElseThrow(() -> new ApiException(ErrorStatus._MEMBERS_NOT_FOUND));
            return MembersDetailResponseDTO.from(members);
        }

    @Override
    public MemberPointDTO getMemberPoint(Members members){
    Long point =  members.getPoint();
        return MemberPointDTO.builder()
                .point(point)
                .id(members.getId())
                .build();
    }

    @Override
    public Members getMemberById(String id){
        return membersRepository.findById(id).orElseThrow(()->new ApiException(ErrorStatus._MEMBERS_NOT_FOUND));
    }

    @Override
    public Role getMemberRoleByid(String id){
        return membersRepository.findById(id).orElseThrow(()->new ApiException(ErrorStatus._MEMBERS_NOT_FOUND)).getRole();
    }

    @Override
    public Long getMembertotalPoint(Members members){
        return membersRepository.findById(members.getId()).orElseThrow(()->new ApiException(ErrorStatus._MEMBERS_NOT_FOUND)).getPoint();
    }

    @Override
    public Long getMemberPenaltyCnt(Members members){
        return membersRepository.findById(members.getId()).orElseThrow(()->new ApiException(ErrorStatus._MEMBERS_NOT_FOUND)).getPenaltyCnt();
    }
}
