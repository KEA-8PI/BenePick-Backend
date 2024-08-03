package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.dto.DrawsRequest;
import com._pi.benepick.domain.draws.dto.DrawsResponse;
import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.draws.entity.Status;
import com._pi.benepick.domain.draws.repository.DrawsRepository;
import com._pi.benepick.domain.draws.service.algorithm.DoubleToSHA256;
import com._pi.benepick.domain.draws.service.algorithm.DrawAlgorithm;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goods.repository.GoodsRepository;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.domain.raffles.repository.RafflesRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DrawsCommandServiceImpl implements DrawsCommandService {

    private final GoodsRepository goodsRepository;
    private final DrawsRepository drawsRepository;
    private final RafflesRepository rafflesRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public DrawsResponse.DrawsResponseByMembersDTO editWinnerStatus(Members members, Long winnerId, DrawsRequest.DrawsRequestDTO dto) {
        if (!(members.getRole().equals(Role.ADMIN))) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        Draws draws = drawsRepository.findById(winnerId).orElseThrow(() -> new ApiException(ErrorStatus._RAFFLES_NOT_COMPLETED));

        Draws newDraws = DrawsRequest.DrawsRequestDTO.updateStatus(draws, dto);
        Draws savedDraws = drawsRepository.save(newDraws);

        return DrawsResponse.DrawsResponseByMembersDTO.from(savedDraws);
    }

    public void drawStart(Long goodsId) throws NoSuchAlgorithmException {
        Goods goods = goodsRepository.findById(goodsId).orElseThrow(() -> new ApiException(ErrorStatus._GOODS_NOT_FOUND));
        // 현재 시각이 응모종료시간보다 이후여야하고, 상태가 PROGRESS 여야 한다.
        if (!(LocalDateTime.now().isAfter(goods.getRaffleEndAt()) && goods.getGoodsStatus().equals(GoodsStatus.PROGRESS))) {
            throw new ApiException(ErrorStatus._BAD_REQUEST);
        }
        List<Raffles> rafflesList = rafflesRepository.findAllByGoodsId(goods);

        double seed = new DrawAlgorithm(-1).getSeed();
        DrawAlgorithm drawAlgorithm = new DrawAlgorithm(seed);
        Raffles winner = drawAlgorithm.drawAlgorithm(rafflesList);
        List<Draws> drawsList = new ArrayList<>();
        drawsList.add(Draws.builder()
                .raffleId(winner)
                .sequence(0)
                .status(Status.WINNER)
                .build());

        String hash = DoubleToSHA256.getSHA256Hash(seed);
        goods.setRandomSeedsAndStatus(hash, GoodsStatus.COMPLETED);

        // 당첨자 추첨.
        for (int i = 0; i < goods.getAmounts() - 1; i++) {
            if (winner != null) {
                rafflesList.remove(winner);
                if (rafflesList.isEmpty()) break;
            }
            // draws 테이블에 추가.
            winner = drawAlgorithm.drawAlgorithm(rafflesList);
            drawsList.add(Draws.builder()
                    .raffleId(winner)
                    .sequence(0)
                    .status(Status.WINNER)
                    .build());
        }

        // 대기자 추첨.
        for (int i = 0; i < goods.getAmounts() * 2; i++) {
            if (winner != null) {
                rafflesList.remove(winner);
                if (rafflesList.isEmpty()) break;
            }
            winner = drawAlgorithm.drawAlgorithm(rafflesList);
            drawsList.add(Draws.builder()
                    .raffleId(winner)
                    .sequence(i+1)
                    .status(Status.WAITLIST)
                    .build());
        }

        drawsRepository.saveAll(drawsList);
        goodsRepository.save(goods);

        // 레디스에 hash 와 seed 저장 필요
        // TODO: 레디스에 hash:seed key value로 저장하는 것 추가 필요.
//        redisTemplate.opsForValue().set(hash, seed);

    }

}
