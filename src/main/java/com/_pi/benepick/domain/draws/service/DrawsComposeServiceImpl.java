package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.dto.DrawsRequest;
import com._pi.benepick.domain.draws.dto.DrawsResponse;
import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.draws.entity.Status;
import com._pi.benepick.domain.draws.service.algorithm.DrawAlgorithm;
import com._pi.benepick.domain.draws.service.algorithm.RaffleDraw;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goods.service.GoodsQueryService;
import com._pi.benepick.domain.goodsCategories.service.GoodsCategoriesQueryService;
import com._pi.benepick.domain.hash.entity.Hash;
import com._pi.benepick.domain.hash.service.HashCommandService;
import com._pi.benepick.domain.hash.service.HashQueryService;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.domain.penaltyHists.dto.PenaltyRequest;
import com._pi.benepick.domain.penaltyHists.service.PenaltyHistsCommandService;
import com._pi.benepick.domain.pointHists.dto.PointHistsRequest;
import com._pi.benepick.domain.pointHists.service.PointHistsCommandService;
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.domain.raffles.service.RafflesQueryService;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DrawsComposeServiceImpl implements DrawsComposeService {

    private final DrawsQueryService drawsQueryService;
    private final DrawsCommandService drawsCommandService;
    private final GoodsQueryService goodsQueryService;
    private final RafflesQueryService rafflesQueryService;
    private final HashCommandService hashCommandService;
    private final PointHistsCommandService pointHistsCommandService;
    private final PenaltyHistsCommandService penaltyHistsCommandService;
    private final HashQueryService hashQueryService;
    private final GoodsCategoriesQueryService goodsCategoriesQueryService;

    public DrawsResponse.DrawsResponseByGoodsListDTO getResultByGoodsId(Long goodsId) {
        List<DrawsResponse.DrawsResponseByGoodsDTO> drawsResponseByGoodsDTOS = (drawsQueryService.findByGoodsId(goodsId)).stream()
                .filter(draws -> draws.getStatus() == Status.WINNER || draws.getStatus() == Status.CONFIRM)
                .map(DrawsResponse.DrawsResponseByGoodsDTO::from)
                .toList();

        return DrawsResponse.DrawsResponseByGoodsListDTO.builder()
                .drawsResponseByGoodsDTOList(drawsResponseByGoodsDTOS)
                .build();
    }

    public DrawsResponse.DrawsResponseByWaitlistGoodsIdListDTO getWaitlistByGoodsId(Members members, Long goodsId) {
        if (!(members.getRole().equals(Role.ADMIN))) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        List<DrawsResponse.DrawsResponseByWaitlistGoodsIdDTO> waitlistGoodsIdDTOS = (drawsQueryService.findByGoodsId(goodsId)).stream()
                .filter(draws -> draws.getStatus() == Status.WAITLIST)
                .map(DrawsResponse.DrawsResponseByWaitlistGoodsIdDTO::from)
                .toList();

        return DrawsResponse.DrawsResponseByWaitlistGoodsIdListDTO.builder()
                .drawsResponseByWaitlistGoodsIdDTOS(waitlistGoodsIdDTOS)
                .build();
    }

    public DrawsResponse.DrawsResponseByWinnerGoodsIdListDTO getWinnersByGoodsId(Members members, Long goodsId) {
        if (!(members.getRole().equals(Role.ADMIN))) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        List<DrawsResponse.DrawsResponseByWinnerGoodsIdDTO> winnerGoodsIdDTOS = (drawsQueryService.findByGoodsId(goodsId)).stream()
                .filter(draws -> draws.getStatus() != Status.WAITLIST && draws.getStatus() != Status.NON_WINNER)
                .map(DrawsResponse.DrawsResponseByWinnerGoodsIdDTO::from)
                .toList();

        return DrawsResponse.DrawsResponseByWinnerGoodsIdListDTO.builder()
                .drawsResponseByWinnerGoodsIdDTOS(winnerGoodsIdDTOS)
                .build();
    }

    public DrawsResponse.DrawsResponseByMembersListDTO getCompleteRafflesByMemberId(Members member) {
        if (!(member.getRole().equals(Role.MEMBER))) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        List<DrawsResponse.DrawsResponseByMembersDTO> drawsResponseByMembersDTOS = (drawsQueryService.findByMemberId(member)).stream()
                .filter(draws -> draws.getRaffleId().getGoodsId().getGoodsStatus() == GoodsStatus.COMPLETED)
                .map(draws -> {
                    String categoryName = goodsCategoriesQueryService.getGoodsCategory(draws.getRaffleId());

                    return DrawsResponse.DrawsResponseByMembersDTO.of(draws, categoryName);
                })
                .toList();

        return DrawsResponse.DrawsResponseByMembersListDTO.builder()
                .drawsResponseByMembersList(drawsResponseByMembersDTOS)
                .build();
    }

    public void downloadExcel(Members members, Long goodsId, HttpServletResponse response) {
        if (!(members.getRole().equals(Role.ADMIN))) {
            throw new ApiException(ErrorStatus._UNAUTHORIZED);
        }

        List<Draws> drawsList = drawsQueryService.findByGoodsId(goodsId);

        // 변경 가능한 리스트 사용
        List<List<String>> data = new ArrayList<>();
        data.add(Arrays.asList("Name", "ID", "Status", "Sequence"));

        for (Draws draws : drawsList) {
            data.add(
                    Arrays.asList(
                            draws.getRaffleId().getMemberId().getName(),
                            String.valueOf(draws.getRaffleId().getMemberId().getId()),
                            draws.getStatus().toString(),
                            String.valueOf(draws.getSequence())
                    )
            );
        }

        // Create a new workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        Cell cell = null;
        Row row = null;

        row = sheet.createRow(0);

        for (int j = 0; j < data.size(); j++) {
            List<String> rowdata = data.get(j);
            for (int i = 0; i < rowdata.size(); i++) {
                cell = row.createCell(i);
                cell.setCellValue(rowdata.get(i));
            }

        }

        int rowCount = 0;
        for (List<String> dto : data) {
            row = sheet.createRow(rowCount++);
            for (int i = 0; i < dto.size(); i++) {
                cell = row.createCell(i);
                cell.setCellValue(dto.get(i));
            }
        }

        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=student.xlsx");

        try {
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            throw new ApiException(ErrorStatus._FILE_OUTPUT_DISABLED);
        }
    }


    public void drawStart(LocalDateTime now) {
        List<Goods> goodsList = goodsQueryService.findByRaffleEndAtBeforeAndGoodsStatus(now);

        for (Goods goods : goodsList) {
            // 현재 시각이 응모종료시간보다 이후여야하고, 상태가 PROGRESS 여야 한다.
            if (!(LocalDateTime.now().isAfter(goods.getRaffleEndAt()) && goods.getGoodsStatus().equals(GoodsStatus.PROGRESS)))
                throw new ApiException(ErrorStatus._BAD_REQUEST);
            List<Raffles> rafflesList = rafflesQueryService.findAllByGoodsIdOrderByPointAsc(goods);

            double seed = DrawAlgorithm.generateSeed();
            Hash savedHash = hashCommandService.saveHash(seed);
            goods.startDraw(savedHash, GoodsStatus.COMPLETED);

            List<Draws> drawsList = RaffleDraw.performDraw(seed, rafflesList, goods);

            for (Draws draws : drawsList) {
                if (draws.getStatus().equals(Status.NON_WINNER)) {
                    nonWinnerPointRefund(draws);
                }
            }
            drawsCommandService.saveDrawsList(drawsList);
        }

    }

    public DrawsResponse.EditWinnerStatus editWinnerStatus(Members members, Long winnerId, DrawsRequest.DrawsRequestDTO dto) {
        if (!(members.getRole().equals(Role.ADMIN))) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        Draws draws = drawsQueryService.findDrawsById(winnerId);
        try {
            if (Status.valueOf(dto.getStatus()).equals(Status.CONFIRM) && !(draws.getStatus().equals(Status.WINNER))) {
                throw new ApiException(ErrorStatus._CONFIRM_REQUIRE_WINNER);
            }
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorStatus._BAD_REQUEST);
        }

        Status newStatus = Status.valueOf(dto.getStatus());
        draws.updateStatus(newStatus);
        switch (newStatus) {
            case CONFIRM:
                changeConfirmRaffleEnd(draws);
                break;

            case NO_SHOW:
                waitlistUpdate(draws);
                noshowPenalty(draws.getRaffleId().getMemberId());
                break;

            case CANCEL:
                waitlistUpdate(draws);
                break;

            default:
                throw new ApiException(ErrorStatus._BAD_REQUEST);
        }

        return DrawsResponse.EditWinnerStatus.builder()
                .id(draws.getId())
                .raffleId(draws.getRaffleId().getId())
                .status(draws.getStatus())
                .sequence(draws.getSequence())
                .build();
    }

    private void waitlistUpdate(Draws draws) {
        List<Draws> drawsList = drawsQueryService.findAllByGoodsIdAndStatus(draws.getRaffleId().getGoodsId(), Status.WAITLIST);
        if (!drawsList.isEmpty()) {
            drawsList.get(0).updateStatus(Status.WINNER);
            for (Draws value : drawsList) {
                value.decreaseSequence();
            }
            drawsCommandService.saveDrawsList(drawsList);
        }
    }

    private void noshowPenalty(Members members) {
        members.updatePenalty(members.getPenaltyCnt() + 5);
        penaltyHistsCommandService.savePenaltyHists(new PenaltyRequest.ChangePenaltyHistDTO(
                5L,"노쑈 패널티 부여",members,members.getPenaltyCnt()
        ));
    }

    private void changeConfirmRaffleEnd(Draws draws) {
        List<Draws> drawsList = drawsQueryService.findAllByGoodsIdAndStatus(draws.getRaffleId().getGoodsId(), Status.WINNER);
        if (drawsList.isEmpty()) {
            drawsList = drawsQueryService.findAllByGoodsIdAndStatus(draws.getRaffleId().getGoodsId(), Status.WAITLIST);
            for (Draws waitDraw : drawsList) {
                nonWinnerPointRefund(waitDraw);
            }
        }
    }

    private void nonWinnerPointRefund(Draws waitDraw) {
        Members waitMembers = waitDraw.getRaffleId().getMemberId();
        waitMembers.increasePoint(Math.round(waitDraw.getRaffleId().getPoint() / 2.0));
        pointHistsCommandService.refundPointHists(new PointHistsRequest.RefundPointHistDTO(
                "낙첨 포인트 반환", waitDraw
        ));
    }

    public DrawsResponse.DrawsResponseResultListDTO verificationSeed(Long goodsId, String hash) {
        Hash findHash = hashQueryService.findByHash(hash);
        double seed = findHash.getSeed();

        Goods goods = goodsQueryService.goodsFindById(goodsId);
        List<Raffles> rafflesList = rafflesQueryService.findAllByGoodsIdOrderByPointAsc(goods);

        List<Draws> drawsListResult = RaffleDraw.performDraw(seed, rafflesList, goods);
        List<DrawsResponse.DrawsResponseResultDTO> drawsResponseResultDTOList = drawsListResult.stream()
                .map(draws -> DrawsResponse.DrawsResponseResultDTO.builder()
                        .status(draws.getStatus())
                        .sequence(draws.getSequence())
                        .memberId(draws.getRaffleId().getMemberId().getId())
                        .memberName(draws.getRaffleId().getMemberId().getName())
                        .build())
                .toList();

        return DrawsResponse.DrawsResponseResultListDTO.builder()
                .drawsList(drawsResponseResultDTOList)
                .build();
    }

}
