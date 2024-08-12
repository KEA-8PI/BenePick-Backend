package com._pi.benepick.domain.raffles.service;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goods.service.GoodsQueryService;
import com._pi.benepick.domain.goodsCategories.service.GoodsCategoriesQueryService;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.domain.penaltyHists.service.PenaltyHistsCommandService;
import com._pi.benepick.domain.pointHists.service.PointHistsCommandService;
import com._pi.benepick.domain.raffles.dto.RafflesRequest;
import com._pi.benepick.domain.raffles.dto.RafflesResponse;
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final GoodsCategoriesQueryService goodsCategoriesQueryService;

    public RafflesResponse.RafflesResponseByMembersListDTO getProgressRafflesByMemberId(Members member) {
        if(member.getRole().equals(Role.ADMIN)) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        List<Raffles> rafflesList = rafflesQueryService.findAllByMemberId(member);

        List<RafflesResponse.RafflesResponseByMembersDTO> rafflesResponseByMembersDTOS = rafflesList.stream()
                .filter(raffles -> raffles.getGoodsId().getGoodsStatus() == GoodsStatus.PROGRESS)
                .map(raffles -> {
                    String categoryName = goodsCategoriesQueryService.getGoodsCategory(raffles);
                    return RafflesResponse.RafflesResponseByMembersDTO.of(raffles, categoryName);
                })
                .toList();

        return RafflesResponse.RafflesResponseByMembersListDTO.builder()
                .rafflesResponseByMembersList(rafflesResponseByMembersDTOS)
                .build();

    }

    public RafflesResponse.RafflesResponseByGoodsListDTO getAllRafflesByGoodsId(Members members, Long goodsId) {
        if (!(members.getRole().equals(Role.ADMIN))) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        List<RafflesResponse.RafflesResponseByGoodsDTO> rafflesResponseByGoodsDTOS = rafflesQueryService
                .findAllBygoodsId(goodsQueryService.goodsFindById(goodsId)).stream()
                .map(RafflesResponse.RafflesResponseByGoodsDTO::from)
                .toList();

        return RafflesResponse.RafflesResponseByGoodsListDTO.builder()
                .rafflesResponseByGoodsList(rafflesResponseByGoodsDTOS)
                .build();
    }

    public RafflesResponse.RafflesResponseByGoodsDTO applyRaffle(Members members, Long goodsId, RafflesRequest.RafflesRequestDTO raffleAddDTO) {
        if (raffleAddDTO.getPoint() <= 0) throw new ApiException(ErrorStatus._RAFFLES_POINT_TOO_LESS);
        if (!(members.getRole().equals(Role.MEMBER))) throw new ApiException(ErrorStatus._UNAUTHORIZED);

        Goods goods = goodsQueryService.goodsFindById(goodsId);
        if (!(goods.getGoodsStatus().equals(GoodsStatus.PROGRESS))) throw new ApiException(ErrorStatus._RAFFLES_CANNOT_APPLY);

        // 포인트 소모 히스토리 반영 부분
        members.decreasePoint(raffleAddDTO.getPoint());
        if (members.getPoint() < 0) {
            throw new ApiException(ErrorStatus._RAFFLES_POINT_TOO_MUCH);
        }
        String comment = "응모 신청";
        pointHistsCommandService.savePointHists(members, comment, -raffleAddDTO.getPoint());

        Raffles raffles = rafflesCommandService.findRaffleByGoodsIdAndMemberId(goods, members, raffleAddDTO.getPoint());

        if (members.getPenaltyCnt() > 0 && raffles.getPoint() >= 100 && raffles.getPenaltyFlag() == 'F') {
            raffles.updatePenaltyFlag('T');
            members.updatePenalty(members.getPenaltyCnt() - 1);
            penaltyHistsCommandService.updatePenaltyHists(members, comment, -1);
        }

        return RafflesResponse.RafflesResponseByGoodsDTO.from(raffles);
    }
}
