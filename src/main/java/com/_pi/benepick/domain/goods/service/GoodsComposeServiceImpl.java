package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.categories.entity.Categories;
import com._pi.benepick.domain.categories.repository.CategoriesRepository;
import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goods.repository.GoodsRepository;
import com._pi.benepick.domain.goodsCategories.entity.GoodsCategories;
import com._pi.benepick.domain.goodsCategories.repository.GoodsCategoriesRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import com._pi.benepick.global.objectStorage.ObjectStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GoodsComposeServiceImpl implements GoodsComposeService {
    private final GoodsRepository goodsRepository;
    private final GoodsCategoriesRepository goodsCategoriesRepository;
    private final CategoriesRepository categoriesRepository;
    private final ObjectStorageService objectStorageService;
    private final GoodsCommandServiceImpl goodsCommandService;

    @Override
    public GoodsResponse.GoodsUploadResponseDTO uploadGoodsFile(MultipartFile file) {
        List<Goods> goodsList = new ArrayList<>();
        List<GoodsCategories> goodsCategoriesList = new ArrayList<>();
        List<File> imageFiles = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

//            // 이미지 파일 추출
//            imageFiles = extractImagesFromWorkbook((XSSFWorkbook) workbook);
//
//            // 이미지 파일을 오브젝트 스토리지에 업로드하고 URL을 가져옴
//            List<String> uploadedUrls = objectStorageService.uploadExcelFile(imageFiles);

            XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
            int urlIndex = 0; // 이미지 URL의 인덱스
            for (Row row : sheet) {
                if (row.getRowNum() == 0) { continue;}
//                // 상품 이미지 번호에 맞는 url 반환
//                String uploadedImageUrl = uploadedUrls.get(((int) row.getCell(2).getNumericCellValue())-1);
                // 상품 응모 상태
                LocalDateTime raffleStartAt = LocalDateTime.parse(row.getCell(6).getStringCellValue());
                LocalDateTime raffleEndAt = LocalDateTime.parse(row.getCell(7).getStringCellValue());
                GoodsStatus status = goodsCommandService.determineGoodsStatus(raffleStartAt, raffleEndAt);
                // 상품 정보
                Goods goods = Goods.builder()
                        .name(row.getCell(0).getStringCellValue())
                        .amounts((long) row.getCell(1).getNumericCellValue())
                        .description(row.getCell(3).getStringCellValue())
                        .price((long) row.getCell(4).getNumericCellValue())
                        .discountPrice((long) row.getCell(5).getNumericCellValue())
                        .raffleStartAt(raffleStartAt)
                        .raffleEndAt(raffleEndAt)
                        .goodsStatus(status)
                        .build();
                goodsList.add(goods);
                // 카테고리
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

//    // 이미지 파일 추출
//    private List<File> extractImagesFromWorkbook(XSSFWorkbook workbook) throws IOException {
//        List<File> imageFiles = new ArrayList<>();
//        // 워크북의 모든 그림 데이터를 반복
//        for (XSSFPictureData pictureData : workbook.getAllPictures()) {
//            log.info(pictureData.toString());
//            String fileExtension = pictureData.suggestFileExtension(); // 파일 확장자 결정
//            File tempFile = File.createTempFile("image", "." + fileExtension); // 임시 파일 생성
//            try (FileOutputStream out = new FileOutputStream(tempFile)) {
//                out.write(pictureData.getData()); // 그림 데이터를 파일로 저장
//            }
//            imageFiles.add(tempFile); // 리스트에 이미지 파일 추가
//        }
//        return imageFiles;
//    }
}

