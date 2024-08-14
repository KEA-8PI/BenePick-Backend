package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.goods.dto.GoodsRequest;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goods.repository.GoodsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com._pi.benepick.domain.goods.entity.GoodsStatus.COMPLETED;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsCommandServiceImpl implements GoodsCommandService {
    private final GoodsRepository goodsRepository;

    @Override
    public Goods createGoods(GoodsRequest.GoodsRequestDTO goodsAddDTO) {
        goodsAddDTO.restrictName();
        GoodsStatus status = determineGoodsStatus(goodsAddDTO.getRaffleStartAt(), goodsAddDTO.getRaffleEndAt());

        Goods goods = goodsAddDTO.toEntity(status);
        goodsRepository.save(goods);

        return goods;
    }

    @Override
    public GoodsStatus determineGoodsStatus(LocalDateTime raffleStartAt, LocalDateTime raffleEndAt) {
        LocalDateTime now = LocalDateTime.now();
        if (raffleStartAt.isAfter(now)) {
            return GoodsStatus.SCHEDULED; // 현재 시간보다 시작 시간이 늦으면 예정 상태
        } else if (raffleEndAt.isBefore(now)) {
            return COMPLETED; // 현재 시간보다 종료 시간이 빠르면 완료 상태
        } else {
            return GoodsStatus.PROGRESS; // 시작 시간이 지나고 종료 시간이 아직 오지 않았으면 진행 중 상태
        }
    }

    @Override
    public void updateGoodsStatus(LocalDateTime now) {
        List<Goods> goodsList = goodsRepository.findByRaffleStartAtBeforeAndGoodsStatus(now, GoodsStatus.SCHEDULED);
        for (Goods goods : goodsList) {
            goods.updateStatus(GoodsStatus.PROGRESS);
        }
    }

}
