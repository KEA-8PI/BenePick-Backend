package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.dto.DrawsRequest;
import com._pi.benepick.domain.draws.dto.DrawsResponse;
import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.draws.entity.Status;
import com._pi.benepick.domain.draws.repository.DrawsRepository;
import com._pi.benepick.domain.draws.service.algorithm.DoubleToSHA256;
import com._pi.benepick.domain.draws.service.algorithm.DrawAlgorithm;
import com._pi.benepick.domain.draws.service.algorithm.RaffleDraw;
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
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
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

        // NO_SHOW로 변경하였을 때
        // TODO: 패널티 히스토리 추가 및 패널티 부여.

        return DrawsResponse.DrawsResponseByMembersDTO.from(savedDraws);
    }

    public void drawStart(Long goodsId) throws NoSuchAlgorithmException {
        Goods goods = goodsRepository.findById(goodsId).orElseThrow(() -> new ApiException(ErrorStatus._GOODS_NOT_FOUND));
        // 현재 시각이 응모종료시간보다 이후여야하고, 상태가 PROGRESS 여야 한다.
        if (!(LocalDateTime.now().isAfter(goods.getRaffleEndAt()) && goods.getGoodsStatus().equals(GoodsStatus.PROGRESS))) {
            throw new ApiException(ErrorStatus._BAD_REQUEST);
        }
        List<Raffles> rafflesList = rafflesRepository.findAllByGoodsId(goods);

        double seed = DrawAlgorithm.generateSeed();
        String hash = DoubleToSHA256.getSHA256Hash(seed);
        goods.startDrawAndUpdateRandomizeSeedsAndStatus(hash, GoodsStatus.COMPLETED);

        List<Draws> drawsList = RaffleDraw.performDraw(seed, rafflesList, goods);

        drawsRepository.saveAll(drawsList);
        goodsRepository.save(goods);

        // 레디스에 hash 와 seed 저장 필요
        // TODO: 레디스에 hash:seed key value로 저장하는 것 추가 필요.
        redisTemplate.opsForValue().set(hash, seed);

    }

    public DrawsResponse.DrawsResponseResultListDTO verificationSeed(Long goodsId, String hash) {
        Object seedValue = redisTemplate.opsForValue().get(hash);
        double seed = 0;
        if (seedValue == null) {
            seed = DrawAlgorithm.generateSeed();
            redisTemplate.opsForValue().set(hash, seed);
        } else {
            seed = (double) seedValue;
        }

        Goods goods = goodsRepository.findById(goodsId).orElseThrow(() -> new ApiException(ErrorStatus._GOODS_NOT_FOUND));
        List<Raffles> rafflesList = rafflesRepository.findAllByGoodsId(goods);

        List<Draws> drawsListResult = RaffleDraw.performDraw(seed, rafflesList, goods);
        List<DrawsResponse.DrawsResponseResultDTO> drawsResponseResultDTOList = drawsListResult.stream()
                .map(draws -> DrawsResponse.DrawsResponseResultDTO.builder()
                        .status(draws.getStatus())
                        .sequence(draws.getSequence())
                        .memberId(draws.getRaffleId().getMemberId().getId())
                        .memberName(draws.getRaffleId().getMemberId().getName())
                        .build())
                .collect(Collectors.toList());

        return DrawsResponse.DrawsResponseResultListDTO.builder()
                .drawsList(drawsResponseResultDTOList)
                .build();
    }

}
