package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.dto.DrawsRequest;
import com._pi.benepick.domain.draws.dto.DrawsResponse;
import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.domain.draws.repository.DrawsRepository;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goodsCategories.repository.GoodsCategoriesRepository;
import com._pi.benepick.domain.draws.entity.Status;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.domain.members.repository.MembersRepository;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DrawsQueryServiceImpl implements DrawsQueryService {

    private final MembersRepository membersRepository;
    private final DrawsRepository drawsRepository;
    private final GoodsCategoriesRepository goodsCategoriesRepository;

    public DrawsResponse.DrawsResponseByGoodsListDTO getResultByGoodsId(Long goodsId) {
        List<DrawsResponse.DrawsResponseByGoodsDTO> drawsResponseByGoodsDTOS = (drawsRepository.findByGoodsId(goodsId)).stream()
                .filter(draws -> draws.getStatus() == Status.WINNER || draws.getStatus() == Status.CONFIRM)
                .map(DrawsResponse.DrawsResponseByGoodsDTO::from)
                .collect(Collectors.toList());

        return DrawsResponse.DrawsResponseByGoodsListDTO.builder()
                .drawsResponseByGoodsDTOList(drawsResponseByGoodsDTOS)
                .build();
    }

    public DrawsResponse.DrawsResponseByGoodsListDTO getWaitlistByGoodsId(Members members, Long goodsId) {
        if (!(members.getRole().equals(Role.ADMIN))) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        List<DrawsResponse.DrawsResponseByGoodsDTO> drawsResponseByGoodsDTOS = (drawsRepository.findByGoodsId(goodsId)).stream()
                .filter(draws -> draws.getStatus() == Status.WAITLIST)
                .map(DrawsResponse.DrawsResponseByGoodsDTO::from)
                .collect(Collectors.toList());

        return DrawsResponse.DrawsResponseByGoodsListDTO.builder()
                .drawsResponseByGoodsDTOList(drawsResponseByGoodsDTOS)
                .build();
    }

    public DrawsResponse.DrawsResponseByGoodsListDTO getWinnersByGoodsId(Members members, Long goodsId) {
        if (!(members.getRole().equals(Role.ADMIN))) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        List<DrawsResponse.DrawsResponseByGoodsDTO> drawsResponseByGoodsDTOS = (drawsRepository.findByGoodsId(goodsId)).stream()
                .filter(draws -> draws.getStatus() != Status.WAITLIST && draws.getStatus() != Status.NON_WINNER)
                .map(DrawsResponse.DrawsResponseByGoodsDTO::from)
                .collect(Collectors.toList());

        return DrawsResponse.DrawsResponseByGoodsListDTO.builder()
                .drawsResponseByGoodsDTOList(drawsResponseByGoodsDTOS)
                .build();
    }

    public DrawsResponse.DrawsResponseByMembersListDTO getCompleteRafflesByMemberId(String memberId) {
        Members members = membersRepository.findById(memberId).orElseThrow(() -> new ApiException(ErrorStatus._UNAUTHORIZED));
        if (!(members.getRole().equals(Role.ADMIN))) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        List<DrawsResponse.DrawsResponseByMembersDTO> drawsResponseByMembersDTOS = (drawsRepository.findByMemberId(memberId)).stream()
                .filter(draws -> draws.getRaffleId().getGoodsId().getGoodsStatus() == GoodsStatus.COMPLETED)
                .map(draws -> {
                    String categoryName = (goodsCategoriesRepository.findByGoodsId(draws.getRaffleId().getGoodsId())).map(goodsCategories -> goodsCategories.getCategoryId().getName()).orElse("NONE");

                    return DrawsResponse.DrawsResponseByMembersDTO.of(draws, categoryName);
                })
                .collect(Collectors.toList());

        return DrawsResponse.DrawsResponseByMembersListDTO.builder()
                .drawsResponseByMembersList(drawsResponseByMembersDTOS)
                .build();
    }

    public DrawsResponse.DrawsResponseByMembersDTO editWinnerStatus(Members members, Long winnerId, DrawsRequest.DrawsRequestDTO dto) {
        if (!(members.getRole().equals(Role.ADMIN))) throw new ApiException(ErrorStatus._UNAUTHORIZED);
        Draws draws = drawsRepository.findById(winnerId).orElseThrow(() -> new ApiException(ErrorStatus._RAFFLES_NOT_COMPLETED));

        Draws newDraws = DrawsRequest.DrawsRequestDTO.updateStatus(draws, dto);
        Draws savedDraws = drawsRepository.save(newDraws);

        return DrawsResponse.DrawsResponseByMembersDTO.from(savedDraws);
    }

    public void downloadExcel(Members members, Long goodsId, HttpServletResponse response) {
        if (!(members.getRole().equals(Role.ADMIN))) throw new ApiException(ErrorStatus._UNAUTHORIZED);

        // Sample data
        List<List<String>> data = Arrays.asList(
                Arrays.asList("Name", "Age", "Location"),
                Arrays.asList("John Doe", "30", "New York"),
                Arrays.asList("Jane Smith", "25", "Los Angeles"),
                Arrays.asList("Mike Johnson", "35", "Chicago")
        );

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
            cell = row.createCell(0);
            cell.setCellValue(dto.get(0));
            cell = row.createCell(1);
            cell.setCellValue(dto.get(1));
            cell = row.createCell(2);
            cell.setCellValue(dto.get(2));
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

}
