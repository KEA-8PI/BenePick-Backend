package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.categories.entity.Categories;
import com._pi.benepick.domain.categories.repository.CategoriesRepository;
import com._pi.benepick.domain.categories.service.CategoriesQueryService;
import com._pi.benepick.domain.goods.dto.GoodsRequest;
import com._pi.benepick.domain.goods.dto.GoodsRequest.GoodsRequestDTO;
import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.goods.dto.GoodsResponse.GoodsAddResponseDTO;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goods.repository.GoodsRepository;
import com._pi.benepick.domain.goodsCategories.entity.GoodsCategories;
import com._pi.benepick.domain.goodsCategories.repository.GoodsCategoriesRepository;
import com._pi.benepick.domain.goodsCategories.service.GoodsCategoriesCommandService;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GoodsComposeServiceImpl implements GoodsComposeService {
    private final GoodsRepository goodsRepository;
    private final GoodsCategoriesCommandService goodsCategoriesCommandService;
    private final CategoriesQueryService categoriesQueryService;
    private final GoodsCommandServiceImpl goodsCommandService;

    // 상품 파일 추가
    @Override
    public GoodsResponse.GoodsUploadResponseDTO uploadGoodsFile(MultipartFile file, Members member) {
        goodsCommandService.checkAdmin(member);
        List<Goods> goodsList = new ArrayList<>();
        List<GoodsCategories> goodsCategoriesList = new ArrayList<>();
        List<GoodsResponse.GoodsAddResponseDTO> goodsAddResponseDTOList = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) { continue;}
                // 상품 정보
                Goods goods = goodsCommandService.addGoods(GoodsRequestDTO.builder()
                    .name(row.getCell(0).getStringCellValue())
                    .amounts((long) row.getCell(1).getNumericCellValue())
                    .description(row.getCell(2).getStringCellValue())
                    .price((long) row.getCell(3).getNumericCellValue())
                    .discountPrice((long) row.getCell(4).getNumericCellValue())
                    .raffleStartAt(LocalDateTime.parse(row.getCell(5).getStringCellValue()))
                    .raffleEndAt(LocalDateTime.parse(row.getCell(6).getStringCellValue()))
                    .build(), member);
                // 카테고리
                String categoryName = row.getCell(7).getStringCellValue();
                Categories category = categoriesQueryService.getCategoriesByName(categoryName);

                goodsCategoriesCommandService.createGoodsCategories(goods, category);

                GoodsResponse.GoodsAddResponseDTO goodsAddResponseDTO = GoodsResponse.GoodsAddResponseDTO.of(goods, category.getName());
                goodsAddResponseDTOList.add(goodsAddResponseDTO);
            }

        } catch (IOException e) {
            throw new ApiException(ErrorStatus._FILE_INPUT_DISABLED);
        }

        return GoodsResponse.GoodsUploadResponseDTO.builder()
                .goodsUploadDTOList(goodsAddResponseDTOList)
                .build();
    }

    @Override
    public GoodsAddResponseDTO addGoods(GoodsRequestDTO goodsAddDTO, Members member) {
        goodsCommandService.checkAdmin(member);

        Goods goods = goodsCommandService.addGoods(goodsAddDTO, member);
        Categories category = categoriesQueryService.getCategoriesByName(goodsAddDTO.getCategory());
        goodsCategoriesCommandService.createGoodsCategories(goods, category);

        return GoodsAddResponseDTO.of(goods, category.getName());
    }


}

