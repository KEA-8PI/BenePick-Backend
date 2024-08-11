package com._pi.benepick.domain.raffles.service;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goods.repository.GoodsRepository;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.domain.penaltyHists.entity.PenaltyHists;
import com._pi.benepick.domain.penaltyHists.repository.PenaltyHistsRepository;
import com._pi.benepick.domain.pointHists.entity.PointHists;
import com._pi.benepick.domain.pointHists.repository.PointHistsRepository;
import com._pi.benepick.domain.raffles.dto.RafflesRequest;
import com._pi.benepick.domain.raffles.dto.RafflesResponse;
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.domain.raffles.repository.RafflesRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RafflesCommandServiceImpl implements RafflesCommandService{

    private final RafflesRepository rafflesRepository;
    private final GoodsRepository goodsRepository;
    private final PenaltyHistsRepository penaltyHistsRepository;
    private final PointHistsRepository pointHistsRepository;

    public RafflesResponse.RafflesResponseByGoodsDTO applyRaffle(Members members, Long goodsId, RafflesRequest.RafflesRequestDTO raffleAddDTO) {
        if (raffleAddDTO.getPoint() < 0) throw new ApiException(ErrorStatus._RAFFLES_POINT_TOO_LESS);
        Goods goods = goodsRepository.findById(goodsId).orElseThrow(() -> new ApiException(ErrorStatus._GOODS_NOT_FOUND));
        if (!(members.getRole().equals(Role.MEMBER))) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        if (!(goods.getGoodsStatus().equals(GoodsStatus.PROGRESS))) throw new ApiException(ErrorStatus._RAFFLES_CANNOT_APPLY);

        // 포인트 소모 히스토리 반영 부분
        members.decreasePoint(raffleAddDTO.getPoint());
        if (members.getPoint() < 0) {
            throw new ApiException(ErrorStatus._RAFFLES_POINT_TOO_MUCH);
        }
        PointHists pointHists = PointHists.builder()
                .memberId(members)
                .content("응모 신청")
                .pointChange(raffleAddDTO.getPoint())
                .totalPoint(members.getPoint())
                .build();
        pointHistsRepository.save(pointHists);

        Optional<Raffles> optionalRaffles = rafflesRepository.findByGoodsIdAndMemberId(goods, members);
        if (optionalRaffles.isPresent()) {
            Raffles raffles = optionalRaffles.get();
            raffles.increasePoint(raffleAddDTO.getPoint());
            if (members.getPenaltyCnt() > 0 && raffles.getPoint() >= 100 && raffles.getPenaltyFlag() == 'F') {
                raffles.updatePenaltyFlag('T');
                PenaltyHists penaltyHists = PenaltyHists.builder()
                        .memberId(members)
                        .content("응모 신청")
                        .totalPenalty((int) (members.getPenaltyCnt() - 1))
                        .penaltyCount(-1)
                        .build();
                members.updatePenalty(members.getPenaltyCnt() - 1);
                penaltyHistsRepository.save(penaltyHists);
            }

            return RafflesResponse.RafflesResponseByGoodsDTO.from(raffles);
        }
        else {
            Raffles raffles;
            if (members.getPenaltyCnt() > 0 && raffleAddDTO.getPoint() >= 100) {
                raffles = RafflesRequest.RafflesRequestDTO.toEntity(members, goods, raffleAddDTO, 'T');
                PenaltyHists penaltyHists = PenaltyHists.builder()
                        .memberId(members)
                        .content("응모 신청")
                        .totalPenalty((int) (members.getPenaltyCnt() - 1))
                        .penaltyCount(-1)
                        .build();
                members.updatePenalty(members.getPenaltyCnt() - 1);
                penaltyHistsRepository.save(penaltyHists);
            } else {
                raffles = RafflesRequest.RafflesRequestDTO.toEntity(members, goods, raffleAddDTO, 'F');
            }
            rafflesRepository.save(raffles);

            return RafflesResponse.RafflesResponseByGoodsDTO.from(raffles);
        }
    }
}
