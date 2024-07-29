package com._pi.benepick.domain.goods.service;


import com._pi.benepick.domain.categories.entity.Categories;
import com._pi.benepick.domain.goodsCategories.entity.GoodsCategories;
import com._pi.benepick.domain.categories.repository.CategoriesRepository;
import com._pi.benepick.domain.goodsCategories.repository.GoodsCategoriesRepository;
import com._pi.benepick.domain.goods.dto.GoodsRequest;
import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;

import com._pi.benepick.domain.goods.repository.GoodsRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsCommandServiceImpl implements GoodsCommandService {

    private final GoodsRepository goodsRepository;
    private final GoodsCategoriesRepository goodsCategoriesRepository;
    private final CategoriesRepository categoriesRepository;

    // 상품 추가 ( 응모 상태 자동 수정 )
    @Override
    public GoodsResponse.GoodsDetailResponseDTO addGoods(GoodsRequest.GoodsRequestDTO goodsAddDTO) {
        // 현재시간과 비교하여 GoodsStatus를 결정
        GoodsStatus status = determineGoodsStatus(goodsAddDTO.getRaffleStartAt(), goodsAddDTO.getRaffleEndAt());

        Goods goods = goodsAddDTO.toEntity(status);
        Goods savedGoods = goodsRepository.save(goods);
        Categories category = categoriesRepository.findByName(goodsAddDTO.getCategory()).orElseThrow(() -> new ApiException(ErrorStatus._CATEGORY_NOT_FOUND));

        GoodsCategories goodsCategories = GoodsCategories.builder()
                .goodsId(savedGoods)
                .categoryId(category)
                .build();
        goodsCategoriesRepository.save(goodsCategories);

        return GoodsResponse.GoodsDetailResponseDTO.of(savedGoods, category.getName());
    }

    //상품 파일 업로드
    public String uploadGoodsFile(MultipartFile file) {
        List<Goods> goodsList = new ArrayList<>();
        List<GoodsCategories> goodsCategoriesList = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0); //첫번째 시트
            for (Row row : sheet) {
                if (row.getRowNum() == 0) { continue; } //헤더 무시

                // 응모상태판단
                LocalDateTime raffleStartAt = LocalDateTime.parse(row.getCell(6).getStringCellValue());
                LocalDateTime raffleEndAt = LocalDateTime.parse(row.getCell(7).getStringCellValue());
                GoodsStatus status = determineGoodsStatus(raffleStartAt, raffleEndAt);

                Goods goods = Goods.builder()
                        .name(row.getCell(0).getStringCellValue())
                        .amounts((long) row.getCell(1).getNumericCellValue())
                        .image(row.getCell(2).getStringCellValue())
                        .description(row.getCell(3).getStringCellValue())
                        .price((long) row.getCell(4).getNumericCellValue())
                        .discountPrice((long) row.getCell(5).getNumericCellValue())
                        .raffleStartAt(raffleStartAt)
                        .raffleEndAt(raffleEndAt)
                        .goodsStatus(status)
                        .build();

                goodsList.add(goods);

                // 상품 별 카테고리
                String categoryName = row.getCell(8).getStringCellValue();
                Categories category = categoriesRepository.findByName(categoryName)
                        .orElseThrow(() -> new ApiException(ErrorStatus._CATEGORY_NOT_FOUND));
                GoodsCategories goodsCategories = GoodsCategories.builder()
                        .goodsId(goods)
                        .categoryId(category)
                        .build();
                goodsCategoriesList.add(goodsCategories);
            }

            goodsRepository.saveAll(goodsList);
            goodsCategoriesRepository.saveAll(goodsCategoriesList);

        } catch (IOException e) {
            return "Failed to read the Excel file: " + e.getMessage();
        }
        return "추가되었습니다.";
    }

    // 상품 수정 ( 응모 상태 자동 수정 )
    @Override
    public GoodsResponse.GoodsDetailResponseDTO updateGoods(Long goodsId, GoodsRequest.GoodsRequestDTO goodsUpdateDTO) {
        Goods goods = goodsRepository.findById(goodsId).orElseThrow(() -> new ApiException(ErrorStatus._GOODS_NOT_FOUND));

        // 현재시간과 비교하여 GoodsStatus를 결정
        GoodsStatus status = determineGoodsStatus(goodsUpdateDTO.getRaffleStartAt(), goodsUpdateDTO.getRaffleEndAt());
        goodsRepository.updateGoods(
                goodsId,
                goodsUpdateDTO.getName(),
                goodsUpdateDTO.getAmounts(),
                goodsUpdateDTO.getImage(),
                goodsUpdateDTO.getDescription(),
                goodsUpdateDTO.getPrice(),
                goodsUpdateDTO.getDiscountPrice(),
                goodsUpdateDTO.getRaffleStartAt(),
                goodsUpdateDTO.getRaffleEndAt(),
                status
        );

        Categories category = categoriesRepository.findByName(goodsUpdateDTO.getCategory()).orElseThrow(() -> new ApiException(ErrorStatus._CATEGORY_NOT_FOUND));
        GoodsCategories goodsCategory = goodsCategoriesRepository.findByGoodsId(goods).orElseThrow(() -> new ApiException(ErrorStatus._GOODS_CATEGORY_NOT_FOUND));

        goodsCategory = goodsCategory.changeCategory(category);
        goodsCategoriesRepository.save(goodsCategory);

        Goods updatedGoods = goodsRepository.findById(goodsId).orElseThrow(() -> new ApiException(ErrorStatus._GOODS_NOT_FOUND));

        return GoodsResponse.GoodsDetailResponseDTO.of(updatedGoods, category.getName());
    }

    // 현재 시간에 따라 GoodsStatus를 결정하는 메소드
    private GoodsStatus determineGoodsStatus(LocalDateTime raffleStartAt, LocalDateTime raffleEndAt) {
        LocalDateTime now = LocalDateTime.now();
        if (raffleStartAt.isAfter(now)) {
            return GoodsStatus.SCHEDULED; // 현재 시간보다 시작 시간이 늦으면 예정 상태
        } else if (raffleEndAt.isBefore(now)) {
            return GoodsStatus.COMPLETED; // 현재 시간보다 종료 시간이 빠르면 완료 상태
        } else {
            return GoodsStatus.PROGRESS; // 시작 시간이 지나고 종료 시간이 아직 오지 않았으면 진행 중 상태
        }
    }

    // 상품 삭제
    @Override
    public GoodsResponse.GoodsDeleteResponseDTO deleteGoods(List<Long> deleteList) {
        List<Goods> goodsList = goodsRepository.findAllById(deleteList);

        if (goodsList.size() != deleteList.size()) {
            throw new ApiException(ErrorStatus._GOODS_NOT_FOUND);
        }
        for (Long goodsId : deleteList) {
            goodsCategoriesRepository.deleteById(goodsId);
        }
        for (Long goodsId : deleteList) {
            goodsRepository.deleteById(goodsId);
        }

        return GoodsResponse.GoodsDeleteResponseDTO.from(goodsList);
    }
}
