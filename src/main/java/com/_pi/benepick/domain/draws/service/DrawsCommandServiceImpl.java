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
import com._pi.benepick.domain.hash.entity.Hash;
import com._pi.benepick.domain.hash.repository.HashsRepository;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.domain.raffles.repository.RafflesRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DrawsCommandServiceImpl implements DrawsCommandService {

    private final GoodsRepository goodsRepository;
    private final DrawsRepository drawsRepository;
    private final RafflesRepository rafflesRepository;
    private final HashsRepository hashsRepository;
    private final MembersRepository membersRepository;

    public DrawsResponse.DrawsResponseByMembersDTO editWinnerStatus(Members members, Long winnerId, DrawsRequest.DrawsRequestDTO dto) {
        if (!(members.getRole().equals(Role.ADMIN))) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        Draws draws = drawsRepository.findById(winnerId).orElseThrow(() -> new ApiException(ErrorStatus._RAFFLES_NOT_COMPLETED));

        Draws newDraws = DrawsRequest.DrawsRequestDTO.updateStatus(draws, dto);
        Draws savedDraws = drawsRepository.save(newDraws);

        // NO_SHOW로 변경하였을 때
        // TODO: 패널티 히스토리 추가 및 패널티 부여.


        return DrawsResponse.DrawsResponseByMembersDTO.from(savedDraws);
    }

    public void drawStart(LocalDateTime now) {
        List<Goods> goodsList = goodsRepository.findByRaffleEndAtBeforeAndGoodsStatus(now, GoodsStatus.PROGRESS);

        for (Goods goods : goodsList) {
            // 현재 시각이 응모종료시간보다 이후여야하고, 상태가 PROGRESS 여야 한다.
            if (!(LocalDateTime.now().isAfter(goods.getRaffleEndAt()) && goods.getGoodsStatus().equals(GoodsStatus.PROGRESS))) {
                throw new ApiException(ErrorStatus._BAD_REQUEST);
            }
            List<Raffles> rafflesList = rafflesRepository.findAllByGoodsId(goods);

            double seed = DrawAlgorithm.generateSeed();
            String hash = DoubleToSHA256.getSHA256Hash(seed);
            Hash hash_entity = Hash.builder()
                    .hash(hash)
                    .seed(seed)
                    .build();
            Hash savedHash = hashsRepository.save(hash_entity);
            goods.startDraw(savedHash, GoodsStatus.COMPLETED);

            List<Draws> drawsList = RaffleDraw.performDraw(seed, rafflesList, goods);

            for (Draws draws : drawsList) {
                if (draws.getStatus().equals(Status.NON_WINNER)) {
                    Members members = draws.getRaffleId().getMemberId();
                    members.updatePoint(-(draws.getRaffleId().getPoint() / 2));
                    membersRepository.save(members);
                }
            }
            drawsRepository.saveAll(drawsList);
            goodsRepository.save(goods);
        }

    }

    public void updateGoodsStatus(LocalDateTime now) {
        List<Goods> goodsList = goodsRepository.findByRaffleStartAtBeforeAndGoodsStatus(now, GoodsStatus.SCHEDULED);
        for (Goods goods : goodsList) {
            goods.updateStatus(GoodsStatus.PROGRESS);
            goodsRepository.save(goods);
        }
    }

    public DrawsResponse.DrawsResponseResultListDTO verificationSeed(Long goodsId, String hash) {
        Optional<Hash> optionalHash = hashsRepository.findByHash(hash);
        double seed = 0;

        if (optionalHash.isPresent()) {
            seed = optionalHash.get().getSeed();
        } else {
            seed = DrawAlgorithm.generateSeed();
            hashsRepository.save(Hash.builder()
                    .seed(seed)
                    .hash(hash)
                    .build());
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
