package com._pi.benepick.domain.raffles.service;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.repository.GoodsRepository;
import com._pi.benepick.domain.goodsCategories.repository.GoodsCategoriesRepository;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.domain.members.repository.MembersRepository;
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
    private final MembersRepository membersRepository;

    public RafflesResponse.RafflesResponseByGoodsDTO applyRaffle(String memberId, Long goodsId, RafflesRequest.RafflesRequestDTO raffleAddDTO) {
        Goods goods = goodsRepository.findById(goodsId).orElseThrow(() -> new ApiException(ErrorStatus._GOODS_NOT_FOUND));
        Members members = membersRepository.findById(memberId).orElseThrow(() -> new ApiException(ErrorStatus._UNAUTHORIZED));
        if (!(members.getRole().equals(Role.MEMBER))) throw new ApiException(ErrorStatus._UNAUTHORIZED);

        // 히스토리 반영 부분
        // TODO: 포인트 소모 히스토리 서비스 로직 구현 필요
        // historyService.addPointUsageHistory(memberId, pointsToDeduct, "Raffle Participation");
        members.updatePoint(raffleAddDTO.getPoint());
        membersRepository.save(members);

        Optional<Raffles> optionalRaffles = rafflesRepository.findByGoodsIdAndMemberId(goods, members);
        if (optionalRaffles.isPresent()) {
            Raffles raffles = optionalRaffles.get();
            Long point = raffleAddDTO.getPoint() + raffles.getPoint();
            raffles = RafflesRequest.RafflesRequestDTO.toEntity(
                    raffles.getId(),
                    members,
                    goods,
                    point
            );
            Raffles savedRaffles = rafflesRepository.save(raffles);

            return RafflesResponse.RafflesResponseByGoodsDTO.from(savedRaffles);
        }
        else {
            Raffles raffles = RafflesRequest.RafflesRequestDTO.toEntity(members, goods, raffleAddDTO);
            Raffles savedRaffles = rafflesRepository.save(raffles);

            return RafflesResponse.RafflesResponseByGoodsDTO.from(savedRaffles);
        }
    }
}
