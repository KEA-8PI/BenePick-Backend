package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.alarm.service.AlarmService;
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
import com._pi.benepick.domain.penaltyHists.entity.PenaltyHists;
import com._pi.benepick.domain.penaltyHists.repository.PenaltyHistsRepository;
import com._pi.benepick.domain.pointHists.entity.PointHists;
import com._pi.benepick.domain.pointHists.repository.PointHistsRepository;
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.domain.raffles.repository.RafflesRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final PenaltyHistsRepository penaltyHistsRepository;
    private final PointHistsRepository pointHistsRepository;
    private final AlarmService alarmService;

    public DrawsResponse.EditWinnerStatus editWinnerStatus(Members members, Long winnerId, DrawsRequest.DrawsRequestDTO dto) {
        if (!(members.getRole().equals(Role.ADMIN))) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        Draws draws = drawsRepository.findById(winnerId).orElseThrow(() -> new ApiException(ErrorStatus._RAFFLES_NOT_COMPLETED));
        try {
            if (Status.valueOf(dto.getStatus()).equals(Status.CONFIRM) && !(draws.getStatus().equals(Status.WINNER))) {
                throw new ApiException(ErrorStatus._CONFIRM_REQUIRE_WINNER);
            }
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorStatus._BAD_REQUEST);
        }

        draws.updateStatus(Status.valueOf(dto.getStatus()));

        // NO_SHOW로 변경하였을 때 패널티 부여.
        if (Status.valueOf(dto.getStatus()).equals(Status.NO_SHOW)) {
            PenaltyHists penaltyHists = PenaltyHists.builder()
                    .memberId(members)
                    .content("노쑈 패널티 부여")
                    .totalPenalty((int) (members.getPenaltyCnt() + 5))
                    .penaltyCount(5)
                    .build();
            members.updatePenalty(members.getPenaltyCnt() + 5);
            membersRepository.save(members);
            penaltyHistsRepository.save(penaltyHists);
        }

        if (Status.valueOf(dto.getStatus()).equals(Status.NO_SHOW) || Status.valueOf(dto.getStatus()).equals(Status.CANCEL)) {
            List<Draws> drawsList = drawsRepository.findAllByGoodsIdAndStatus(draws.getRaffleId().getGoodsId().getId(), Status.WAITLIST);
            System.out.println("drawList Size : " + draws.getRaffleId().getGoodsId().getId());
            if (!drawsList.isEmpty()) {
                drawsList.get(0).updateStatus(Status.WINNER);
                Members additionalWinner = drawsList.get(0).getRaffleId().getMemberId();
                String url = "http://localhost:3000/goods/" + draws.getRaffleId().getGoodsId();
                String contents = alarmService.getAdditionalCongratulationsMessage(additionalWinner.getId(), additionalWinner.getName(), url);
                alarmService.saveMessage(additionalWinner.getId(), additionalWinner.getName(), contents);

                for (Draws value : drawsList) {
                    value.decreaseSequence();
                }

                drawsRepository.saveAll(drawsList);
            }
        }

        if (dto.getStatus().equals("CONFIRM")) {
            List<Draws> drawsList = drawsRepository.findAllByGoodsIdAndStatus(draws.getRaffleId().getGoodsId().getId(), Status.WINNER);
            if (drawsList.isEmpty()) {
                drawsList = drawsRepository.findAllByGoodsIdAndStatus(draws.getRaffleId().getGoodsId().getId(), Status.WAITLIST);
                for (Draws waitDraw : drawsList) {
                    Members waitMembers = waitDraw.getRaffleId().getMemberId();
                    waitMembers.increasePoint(Math.round(waitDraw.getRaffleId().getPoint() / 2.0));
                    PointHists pointHists = PointHists.builder()
                            .memberId(waitMembers)
                            .content("낙첨 포인트 반환")
                            .pointChange(Math.round(waitDraw.getRaffleId().getPoint() / 2.0))
                            .totalPoint(waitMembers.getPoint())
                            .build();
                    pointHistsRepository.save(pointHists);
                    membersRepository.save(waitMembers);
                }
            }
        }

        return DrawsResponse.EditWinnerStatus.builder()
                .id(draws.getId())
                .raffleId(draws.getRaffleId().getId())
                .status(draws.getStatus())
                .sequence(draws.getSequence())
                .build();
    }

    public void drawStart(LocalDateTime now) {
        List<Goods> goodsList = goodsRepository.findByRaffleEndAtBeforeAndGoodsStatus(now, GoodsStatus.PROGRESS);
        // TODO: 패널티에 따라 응모하는 포인트 차감하고 패널티 횟수도 감소.

        for (Goods goods : goodsList) {
            // 현재 시각이 응모종료시간보다 이후여야하고, 상태가 PROGRESS 여야 한다.
            if (!(LocalDateTime.now().isAfter(goods.getRaffleEndAt()) && goods.getGoodsStatus().equals(GoodsStatus.PROGRESS))) {
                throw new ApiException(ErrorStatus._BAD_REQUEST);
            }
            List<Raffles> rafflesList = rafflesRepository.findAllByGoodsIdOrderByPointAsc(goods);

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
                    members.increasePoint(Math.round(draws.getRaffleId().getPoint() / 2.0));
                    PointHists pointHists = PointHists.builder()
                            .memberId(members)
                            .content("낙첨 포인트 반환")
                            .pointChange(Math.round(draws.getRaffleId().getPoint() / 2.0))
                            .totalPoint(members.getPoint())
                            .build();
                    pointHistsRepository.save(pointHists);
                    membersRepository.save(members);
                } else if (draws.getStatus().equals(Status.WINNER)) {
                    Members members = draws.getRaffleId().getMemberId();
                    String url = "http://localhost:3000/goods/" + draws.getRaffleId().getGoodsId();
                    String contents = alarmService.getCongratulationsMessage(members.getId(), members.getName(), url);
                    alarmService.saveMessage(members.getId(), members.getName(), contents);
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
        List<Raffles> rafflesList = rafflesRepository.findAllByGoodsIdOrderByPointAsc(goods);

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
