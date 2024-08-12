package com._pi.benepick.domain.pointHists.service;

import com._pi.benepick.domain.pointHists.entity.PointHists;
import com._pi.benepick.domain.pointHists.repository.PointHistsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointHistsQueryServiceImpl implements PointHistsQueryService{
    private final PointHistsRepository pointHistsRepository;

    @Override
    public Page<PointHists> getMemberPointHists(Pageable pageable, String id){
        return pointHistsRepository.findAllByMemberId(pageable,id);
    }

    @Override
    public int getMemberPointHistCnt(String id){
        return pointHistsRepository.countAllByMemberId_Id(id);
    }

}
