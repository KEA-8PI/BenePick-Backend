package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.dto.DrawsResponse;
import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.draws.repository.DrawsRepository;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.draws.entity.Status;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DrawsQueryServiceImpl implements DrawsQueryService {
    private final DrawsRepository drawsRepository;

    public DrawsResponse.DrawsResponseByGoodsListDTO getResultByGoodsId(Long goodsId) {
        List<DrawsResponse.DrawsResponseByGoodsDTO> drawsResponseByGoodsDTOS = (drawsRepository.findByGoodsId(goodsId)).stream()
                .filter(draws -> draws.getStatus() == Status.WINNER || draws.getStatus() == Status.CONFIRM)
                .map(DrawsResponse.DrawsResponseByGoodsDTO::from)
                .toList();

        return DrawsResponse.DrawsResponseByGoodsListDTO.builder()
                .drawsResponseByGoodsDTOList(drawsResponseByGoodsDTOS)
                .build();
    }

    public DrawsResponse.DrawsResponseByWaitlistGoodsIdListDTO getWaitlistByGoodsId(Members members, Long goodsId) {
        if (!(members.getRole().equals(Role.ADMIN))) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        List<DrawsResponse.DrawsResponseByWaitlistGoodsIdDTO> waitlistGoodsIdDTOS = (drawsRepository.findByGoodsId(goodsId)).stream()
                .filter(draws -> draws.getStatus() == Status.WAITLIST)
                .map(DrawsResponse.DrawsResponseByWaitlistGoodsIdDTO::from)
                .toList();

        return DrawsResponse.DrawsResponseByWaitlistGoodsIdListDTO.builder()
                .drawsResponseByWaitlistGoodsIdDTOS(waitlistGoodsIdDTOS)
                .build();
    }

    public DrawsResponse.DrawsResponseByWinnerGoodsIdListDTO getWinnersByGoodsId(Members members, Long goodsId) {
        if (!(members.getRole().equals(Role.ADMIN))) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        List<DrawsResponse.DrawsResponseByWinnerGoodsIdDTO> winnerGoodsIdDTOS = (drawsRepository.findByGoodsId(goodsId)).stream()
                .filter(draws -> draws.getStatus() != Status.WAITLIST && draws.getStatus() != Status.NON_WINNER)
                .map(DrawsResponse.DrawsResponseByWinnerGoodsIdDTO::from)
                .toList();

        return DrawsResponse.DrawsResponseByWinnerGoodsIdListDTO.builder()
                .drawsResponseByWinnerGoodsIdDTOS(winnerGoodsIdDTOS)
                .build();
    }

    public Draws findDrawsById(Long drawsId) {
        return drawsRepository.findById(drawsId).orElseThrow(() -> new ApiException(ErrorStatus._RAFFLES_NOT_COMPLETED));
    }

    public List<Draws> findAllByGoodsIdAndStatus(Goods goods, Status status) {
        return drawsRepository.findAllByGoodsIdAndStatus(goods.getId(), status);
    }

    public List<Draws> findByGoodsId(Long goodsId) {
        return drawsRepository.findByGoodsId(goodsId);
    }

    public List<Draws> findByMemberId(Members member) {
        return drawsRepository.findByMemberId(member.getId());
    }

    public void downloadExcel(Members members, Long goodsId, HttpServletResponse response) {
        if (!(members.getRole().equals(Role.ADMIN))) {
            throw new ApiException(ErrorStatus._UNAUTHORIZED);
        }

        List<Draws> drawsList = drawsRepository.findByGoodsId(goodsId);

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

    @Override
    public Double getAveragePointByGoodsIdAndStatuses(Long goodsId, List<Status> statuses){
        return drawsRepository.findAveragePointByGoodsIdAndStatuses(goodsId, statuses);
    }

    @Override
    public Long countByRaffleIdsAndStatuses(List<Long> rafflesId, List<Status> statuses){
        return drawsRepository.countByRaffleIdsAndStatuses(rafflesId, statuses);
    }

    @Override
    public List<Draws> getDrawsByGoodsIdAndStatuses(Long goodsId, List<Status> statuses){
        return drawsRepository.findDrawsByGoodsIdAndStatuses(goodsId, statuses);
    }
}
