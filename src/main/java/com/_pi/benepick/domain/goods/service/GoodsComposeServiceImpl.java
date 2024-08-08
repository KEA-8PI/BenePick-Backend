package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.categories.entity.Categories;
import com._pi.benepick.domain.categories.repository.CategoriesRepository;
import com._pi.benepick.domain.goods.dto.GoodsRequest;
import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goods.repository.GoodsRepository;
import com._pi.benepick.domain.goodsCategories.entity.GoodsCategories;
import com._pi.benepick.domain.goodsCategories.repository.GoodsCategoriesRepository;
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
    private final GoodsCategoriesRepository goodsCategoriesRepository;
    private final CategoriesRepository categoriesRepository;
    private final MembersRepository membersRepository;

    // 상품 추가 ( 응모 상태 자동 수정 )
    @Override
    public GoodsResponse.GoodsAddResponseDTO addGoods(GoodsRequest.GoodsRequestDTO goodsAddDTO, Members member) {
        checkAdmin(member);
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

        return GoodsResponse.GoodsAddResponseDTO.of(savedGoods, category.getName());
    }

    // 상품 수정 ( 응모 상태 자동 수정 )
    @Override
    public GoodsResponse.GoodsAddResponseDTO updateGoods(Long goodsId, GoodsRequest.GoodsRequestDTO goodsUpdateDTO, Members member) {
        checkAdmin(member);
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
        return GoodsResponse.GoodsAddResponseDTO.of(updatedGoods, category.getName());
    }

    // 상품 파일 추가
    @Override
    public GoodsResponse.GoodsUploadResponseDTO uploadGoodsFile(MultipartFile file, Members member) {
        checkAdmin(member);
        List<Goods> goodsList = new ArrayList<>();
        List<GoodsCategories> goodsCategoriesList = new ArrayList<>();
        List<GoodsResponse.GoodsAddResponseDTO> goodsAddResponseDTOList = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) { continue;}
                // 상품 응모 상태
                LocalDateTime raffleStartAt = LocalDateTime.parse(row.getCell(5).getStringCellValue());
                LocalDateTime raffleEndAt = LocalDateTime.parse(row.getCell(6).getStringCellValue());
                GoodsStatus status = determineGoodsStatus(raffleStartAt, raffleEndAt);
                // 상품 정보
                Goods goods = Goods.builder()
                        .name(row.getCell(0).getStringCellValue())
                        .amounts((long) row.getCell(1).getNumericCellValue())
                        .description(row.getCell(2).getStringCellValue())
                        .price((long) row.getCell(3).getNumericCellValue())
                        .discountPrice((long) row.getCell(4).getNumericCellValue())
                        .raffleStartAt(raffleStartAt)
                        .raffleEndAt(raffleEndAt)
                        .goodsStatus(status)
                        .build();
                goodsList.add(goods);
                // 카테고리
                String categoryName = row.getCell(7).getStringCellValue();
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

            for (Goods goods : goodsList) {
                Optional<GoodsCategories> goodsCategoriesOptional = goodsCategoriesRepository.findByGoodsId(goods);
                if (goodsCategoriesOptional.isPresent()) {
                    Categories category = goodsCategoriesOptional.get().getCategoryId();
                    GoodsResponse.GoodsAddResponseDTO goodsAddResponseDTO = GoodsResponse.GoodsAddResponseDTO.of(goods, category.getName());
                    goodsAddResponseDTOList.add(goodsAddResponseDTO);
                }
            }

        } catch (IOException e) {
            throw new ApiException(ErrorStatus._FILE_INPUT_DISABLED);
        }

        return GoodsResponse.GoodsUploadResponseDTO.builder()
                .goodsUploadDTOList(goodsAddResponseDTOList)
                .build();
    }

    // 상품 목록 조회 (+ 검색)
    @Override
    public GoodsResponse.GoodsListResponseDTO getGoodsList(Integer page, Integer size, String keyword, Members member) {
        checkAdmin(member);

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Goods> goodsPage;

        if (keyword != null && !keyword.isEmpty()) {
            goodsPage = goodsRepository.findByNameContainingIgnoreCase(keyword, pageRequest);
        } else {
            goodsPage = goodsRepository.findAll(pageRequest);
        }

        List<GoodsResponse.GoodsResponseDTO> goodsDTOList = goodsPage.getContent().stream()
                .map(GoodsResponse.GoodsResponseDTO::from)
                .toList();

        return GoodsResponse.GoodsListResponseDTO.builder()
                .goodsDTOList(goodsDTOList)
                .build();
    }

    // 상품 삭제
    @Override
    public GoodsResponse.GoodsDeleteResponseDTO deleteGoods(List<Long> deleteList, Members member) {
        checkAdmin(member);

        List<Long> deletedList = new ArrayList<>();

        for(Long id:deleteList){
            Goods goods = goodsRepository.findById(id).orElseThrow(()->new ApiException(ErrorStatus._GOODS_NOT_FOUND));
            goodsRepository.delete(goods);
            deletedList.add(id);
        }
        return GoodsResponse.GoodsDeleteResponseDTO.from(deletedList);
    }

    // 현재 시간에 따라 GoodsStatus를 결정하는 메소드
    public GoodsStatus determineGoodsStatus(LocalDateTime raffleStartAt, LocalDateTime raffleEndAt) {
        LocalDateTime now = LocalDateTime.now();
        if (raffleStartAt.isAfter(now)) {
            return GoodsStatus.SCHEDULED; // 현재 시간보다 시작 시간이 늦으면 예정 상태
        } else if (raffleEndAt.isBefore(now)) {
            return GoodsStatus.COMPLETED; // 현재 시간보다 종료 시간이 빠르면 완료 상태
        } else {
            return GoodsStatus.PROGRESS; // 시작 시간이 지나고 종료 시간이 아직 오지 않았으면 진행 중 상태
        }
    }

    // 관리자 확인 로직
    private void checkAdmin(Members member) {
        if (membersRepository.findById(member.getId()).get().getRole() == Role.MEMBER) {
            throw new ApiException(ErrorStatus._UNAUTHORIZED);
        }
    }
}

