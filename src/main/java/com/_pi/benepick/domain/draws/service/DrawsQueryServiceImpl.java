package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.draws.repository.DrawsRepository;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.draws.entity.Status;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DrawsQueryServiceImpl implements DrawsQueryService {
    private final DrawsRepository drawsRepository;

    public Draws findDrawsById(Long drawsId) {
        return drawsRepository.findById(drawsId).orElseThrow(() -> new ApiException(ErrorStatus._RAFFLES_NOT_COMPLETED));
    }

    public List<Draws> findAllByGoodsIdAndStatus(Goods goods, Status status) {
        return drawsRepository.findAllByGoodsIdAndStatus(goods.getId(), status);
    }

    public List<Draws> findByGoodsId(Long goodsId) {
        return drawsRepository.findByGoodsId(goodsId);
    }

    public List<Draws> findByMemberId(Members member) {
        return drawsRepository.findByMemberId(member.getId());
    }
}
