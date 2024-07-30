package com._pi.benepick.domain.draws.controller;

import com._pi.benepick.domain.draws.dto.DrawsRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com._pi.benepick.domain.draws.dto.DrawsResponse;
import com._pi.benepick.domain.draws.entity.Status;
import com._pi.benepick.domain.draws.service.DrawsQueryService;
import com._pi.benepick.domain.raffles.controller.RafflesController;
import com._pi.benepick.global.common.response.ApiResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/draws")
@Tag(name = "Draws", description = "추첨 API")
public class DrawsController {

    private final DrawsQueryService drawsQueryService;

    @Operation(summary = "상품별 당첨자 조회", description = "상품에 응모한 사원들 중 추첨 결과가 대기가 아닌 사원들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/winners/{goodsId}")
    public ApiResponse<DrawsResponse.DrawsResponseByGoodsListDTO> getWinnersByGoodsId(@PathVariable Long goodsId) {
        return ApiResponse.onSuccess(drawsQueryService.getWinnersByGoodsId(goodsId));
    }

    @Operation(summary = "상품별 대기자 조회", description = "상품에 응모한 사원들 중 추첨 결과가 대기중인 사원들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/waitlist/{goodsId}")
    public ApiResponse<DrawsResponse.DrawsResponseByGoodsListDTO> getWaitlistByGoodsId(@PathVariable Long goodsId) {
        return ApiResponse.onSuccess(drawsQueryService.getWaitlistByGoodsId(goodsId));
    }

    // 인증인가 구현되고 token 받게 되면 수정 예정
    @Operation(summary = "사원별 종료된 응모 내역 조회", description = "사원이 응모한 추첨 종료된 상품들과 그 내역을 확인할 수 있습니다.")
    @GetMapping("/complete/raffles/{memberId}")
    public ApiResponse<DrawsResponse.DrawsResponseByMembersListDTO> getCompleteRafflesByMemberId(@PathVariable String memberId) {
        return ApiResponse.onSuccess(drawsQueryService.getCompleteRafflesByMemberId(memberId));
    }

    @Operation(summary = "당첨자 상태 관리 - Mockup API", description = "당첨자들의 상태를 관리할 수 있습니다.")
    @PatchMapping("/winners/edit/{winnersId}")
    public ApiResponse<DrawsResponse.DrawsResponseByMembersDTO> editWinnerStatus(@PathVariable Long winnersId, @RequestBody DrawsRequest.DrawsRequestDTO dto) {

        return ApiResponse.onSuccess(drawsQueryService.editWinnerStatus(winnersId, dto));
    }

    @GetMapping("/download/{goodsId}")
    public void downloadExcel(@PathVariable Long goodsId, HttpServletResponse response) throws IOException {
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
        } catch (IOException e) {
            log.error("error 발생");
            throw new RuntimeException(e);
        } finally {
            workbook.close();
        }

//        // Write the output to a byte array
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        workbook.write(outputStream);
//        workbook.close();
//
//        byte[] excelContent = outputStream.toByteArray();
//
//        return excelContent;
    }

    @AllArgsConstructor
    private static class Result {
        @JsonProperty("status")
        private Status status;

        @JsonProperty("winner_id")
        private Long winner_id;

        @JsonProperty("updated_at")
        private LocalDateTime updated_at;
    }

}
