package com._pi.benepick.domain.raffles.service;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goods.service.GoodsQueryService;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.domain.penaltyHists.dto.PenaltyRequest;
import com._pi.benepick.domain.penaltyHists.service.PenaltyHistsCommandService;
import com._pi.benepick.domain.pointHists.dto.PointHistsRequest;
import com._pi.benepick.domain.pointHists.service.PointHistsCommandService;
import com._pi.benepick.domain.raffles.dto.RafflesRequest.RafflesRequestDTO;
import com._pi.benepick.domain.raffles.dto.RafflesResponse.*;
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RafflesComposeServiceImpl implements RafflesComposeService {

    private final RafflesCommandService rafflesCommandService;
    private final RafflesQueryService rafflesQueryService;

    private final GoodsQueryService goodsQueryService;
    private final PenaltyHistsCommandService penaltyHistsCommandService;
    private final PointHistsCommandService pointHistsCommandService;

    public RafflesResponseByGoodsListDTO getAllRafflesByGoodsId(Members members, Long goodsId) {
        if (!(members.getRole().equals(Role.ADMIN))) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        List<RafflesResponseByGoodsDTO> rafflesResponseByGoodsDTOS = rafflesQueryService
                .findAllByGoodsId(goodsQueryService.getGoodsById(goodsId)).stream()
                .map(RafflesResponseByGoodsDTO::from)
                .toList();

        return RafflesResponseByGoodsListDTO.builder()
                .rafflesResponseByGoodsList(rafflesResponseByGoodsDTOS)
                .build();
    }

    public RafflesResponseByGoodsDTO applyRaffle(Members members, Long goodsId, RafflesRequestDTO raffleAddDTO) {
        if (!(members.getRole().equals(Role.MEMBER))) throw new ApiException(ErrorStatus._UNAUTHORIZED);

        Goods goods = goodsQueryService.getGoodsById(goodsId);
        if (!(goods.getGoodsStatus().equals(GoodsStatus.PROGRESS))) throw new ApiException(ErrorStatus._RAFFLES_CANNOT_APPLY);

        // 포인트 소모 히스토리 반영 부분
        members.decreasePoint(raffleAddDTO.getPoint());
        if (members.getPoint() < 0) {
            throw new ApiException(ErrorStatus._RAFFLES_POINT_TOO_MUCH);
        }
        String comment = "응모 신청";

        pointHistsCommandService.createPointHists(new PointHistsRequest.ChangePointHistDTO(
                -raffleAddDTO.getPoint(), comment, members.getPoint(), members
        ));

        Raffles raffles = rafflesCommandService.fetchOrInitializeRaffle(goods, members, raffleAddDTO.getPoint());

        if (members.getPenaltyCnt() > 0 && raffles.getPoint() >= 100 && raffles.getPenaltyFlag() == 'F') {
            raffles.updatePenaltyFlag('T');
            members.updatePenalty(members.getPenaltyCnt() - 1);
            penaltyHistsCommandService.createPenaltyHists(new PenaltyRequest.ChangePenaltyHistDTO(
                    -1L,comment,members,members.getPenaltyCnt()
            ));
        }

        return RafflesResponseByGoodsDTO.from(raffles);
    }

    public CurrentStateByGoodsListDTO getCurrentStateByGoods(Long goodsId) {
        List<Raffles> rafflesList = rafflesQueryService.findAllByGoodsIdOrderByPointDesc(goodsQueryService.getGoodsById(goodsId));

        List<CurrentStateByGoodsDTO> currentStateByGoodsDTOS = new ArrayList<>();
        for (int i = 0; i < Math.min(5, rafflesList.size()); i++) {
            currentStateByGoodsDTOS.add(CurrentStateByGoodsDTO.builder()
                    .grade(i + 1)
                    .point(rafflesList.get(i).getPoint())
                    .build());
        }

        if (rafflesList.size() > 5) {
            Long totalRemainingPoints = rafflesList.subList(5, rafflesList.size())
                    .stream()
                    .mapToLong(Raffles::getPoint)
                    .sum();

            currentStateByGoodsDTOS.add(CurrentStateByGoodsDTO.builder()
                    .grade(6)
                    .point(totalRemainingPoints)
                    .build());
        }

        Long total = pointTotal(rafflesList);
        return CurrentStateByGoodsListDTO.builder()
                .currentStateByGoodsDTOList(currentStateByGoodsDTOS)
                .average(Math.round((float) total / rafflesList.size()))
                .total(total)
                .build();
    }

    private Long pointTotal(List<Raffles> rafflesList) {
        Long total = 0L;
        for (Raffles raffles : rafflesList) {
            total += raffles.getPoint();
        }
        return total;
    }
}
