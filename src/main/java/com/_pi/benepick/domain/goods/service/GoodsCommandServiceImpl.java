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
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
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
    public GoodsResponse.GoodsAddResponseDTO addGoods(GoodsRequest.GoodsRequestDTO goodsAddDTO) {
        // 현재시간과 비교하여 GoodsStatus를 결정
        GoodsStatus status = determineGoodsStatus(goodsAddDTO.getRaffleStartAt(), goodsAddDTO.getRaffleEndAt());

        Goods goods = goodsAddDTO.toEntity(status);
        Goods savedGoods = goodsRepository.save(goods);
        Categories category = categoriesRepository.findByName(goodsAddDTO.getCategory())
                .orElseThrow(() -> new ApiException(ErrorStatus._CATEGORY_NOT_FOUND));

        GoodsCategories goodsCategories = GoodsCategories.builder()
                .goodsId(savedGoods)
                .categoryId(category)
                .build();
        goodsCategoriesRepository.save(goodsCategories);

        return GoodsResponse.GoodsAddResponseDTO.of(savedGoods, category.getName());
    }

    // 상품 파일 업로드
    @Override
    public GoodsResponse.GoodsUploadResponseDTO uploadGoodsFile(MultipartFile file) {
        List<Goods> goodsList = new ArrayList<>();
        List<GoodsCategories> goodsCategoriesList = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            // 이미지 데이터 리스트
            List<byte[]> imageDataList = new ArrayList<>();
            List<XSSFPictureData> pictures = workbook.getAllPictures();
            for (XSSFPictureData picture : pictures) {
                imageDataList.add(picture.getData());
            }
            int imageIndex = 0;

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {continue;} // 헤더 무시

                // 응모 상태 판단
                LocalDateTime raffleStartAt = LocalDateTime.parse(row.getCell(6).getStringCellValue());
                LocalDateTime raffleEndAt = LocalDateTime.parse(row.getCell(7).getStringCellValue());
                GoodsStatus status = determineGoodsStatus(raffleStartAt, raffleEndAt);
                // 이미지 추출
                byte[] imageData = imageIndex < imageDataList.size() ? imageDataList.get(imageIndex) : new byte[0];
                imageIndex++;
                String imageBase64 = Base64.getEncoder().encodeToString(imageData);// 이미지 데이터를 Base64 문자열로 변환
                // 상품 정보
                Goods goods = Goods.builder()
                        .name(row.getCell(0).getStringCellValue())
                        .amounts((long) row.getCell(1).getNumericCellValue())
                        .image(imageBase64) // Base64 문자열로 저장
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
            return GoodsResponse.GoodsUploadResponseDTO.createFailureResponse();
        }
        return GoodsResponse.GoodsUploadResponseDTO.createSuccessResponse();
    }

    // 상품 수정 ( 응모 상태 자동 수정 )
    @Override
    public GoodsResponse.GoodsAddResponseDTO updateGoods(Long goodsId, GoodsRequest.GoodsRequestDTO goodsUpdateDTO) {
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new ApiException(ErrorStatus._GOODS_NOT_FOUND));

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

        Categories category = categoriesRepository.findByName(goodsUpdateDTO.getCategory())
                .orElseThrow(() -> new ApiException(ErrorStatus._CATEGORY_NOT_FOUND));
        GoodsCategories goodsCategory = goodsCategoriesRepository.findByGoodsId(goods)
                .orElseThrow(() -> new ApiException(ErrorStatus._GOODS_CATEGORY_NOT_FOUND));

        goodsCategory = goodsCategory.changeCategory(category);
        goodsCategoriesRepository.save(goodsCategory);

        Goods updatedGoods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new ApiException(ErrorStatus._GOODS_NOT_FOUND));

        return GoodsResponse.GoodsAddResponseDTO.of(updatedGoods, category.getName());
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
