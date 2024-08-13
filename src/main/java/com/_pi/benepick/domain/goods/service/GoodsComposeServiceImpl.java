package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.categories.entity.Categories;
import com._pi.benepick.domain.categories.service.CategoriesQueryService;
import com._pi.benepick.domain.goods.dto.GoodsRequest;
import com._pi.benepick.domain.goods.dto.GoodsRequest.GoodsRequestDTO;
import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.goods.dto.GoodsResponse.GoodsAddResponseDTO;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsFilter;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goods.repository.GoodsRepository;
import com._pi.benepick.domain.goodsCategories.entity.GoodsCategories;
import com._pi.benepick.domain.goodsCategories.service.GoodsCategoriesCommandService;
import com._pi.benepick.domain.goodsCategories.service.GoodsCategoriesQueryService;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com._pi.benepick.domain.goods.entity.GoodsStatus.COMPLETED;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsComposeServiceImpl implements GoodsComposeService {
    private final GoodsRepository goodsRepository;
    private final GoodsCategoriesCommandService goodsCategoriesCommandService;
    private final GoodsCategoriesQueryService goodsCategoriesQueryService;
    private final CategoriesQueryService categoriesQueryService;
    private final GoodsCommandService goodsCommandService;
    private final GoodsQueryService goodsQueryService;

    // 상품 파일 추가
    @Override
    public GoodsResponse.GoodsUploadResponseDTO uploadGoodsFile(MultipartFile file, Members member) {
        if (member.getRole() == Role.MEMBER) {
            throw new ApiException(ErrorStatus._ACCESS_DENIED_FOR_MEMBER);
        }
        List<GoodsResponse.GoodsAddResponseDTO> goodsAddResponseDTOList = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) { continue;}

                String name = row.getCell(0).getStringCellValue();
                if (name.length() > 50) {
                    name = name.substring(0, 50);
                }

                // 상품 정보
                Goods goods = goodsCommandService.createGoods(GoodsRequestDTO.builder()
                    .name(name)
                    .amounts((long) row.getCell(1).getNumericCellValue())
                    .description(row.getCell(2).getStringCellValue())
                    .price((long) row.getCell(3).getNumericCellValue())
                    .discountPrice((long) row.getCell(4).getNumericCellValue())
                    .raffleStartAt(LocalDateTime.parse(row.getCell(5).getStringCellValue()))
                    .raffleEndAt(LocalDateTime.parse(row.getCell(6).getStringCellValue()))
                    .build());
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

    // 상품 추가 ( 응모 상태 자동 수정 )
    @Override
    public GoodsAddResponseDTO addGoods(GoodsRequestDTO goodsAddDTO, Members member) {
        if (member.getRole() == Role.MEMBER) {
            throw new ApiException(ErrorStatus._ACCESS_DENIED_FOR_MEMBER);
        }
        goodsAddDTO.restrictName();

        Goods goods = goodsCommandService.createGoods(goodsAddDTO);
        Categories category = categoriesQueryService.getCategoriesByName(goodsAddDTO.getCategory());
        goodsCategoriesCommandService.createGoodsCategories(goods, category);

        return GoodsAddResponseDTO.of(goods, category.getName());
    }


    // 상품 수정 ( 응모 상태 자동 수정 )
    @Override
    public GoodsResponse.GoodsAddResponseDTO updateGoods(Long goodsId, GoodsRequest.GoodsRequestDTO goodsUpdateDTO, Members member) {
        if (member.getRole() == Role.MEMBER) {
            throw new ApiException(ErrorStatus._ACCESS_DENIED_FOR_MEMBER);
        }
        goodsUpdateDTO.restrictName();

        Goods goods = goodsQueryService.getGoodsById(goodsId);
        if(goods.getGoodsStatus() == COMPLETED){
            throw new ApiException(ErrorStatus._COMPLETED_GOODS);
        }
        GoodsStatus status = goodsCommandService.determineGoodsStatus(goodsUpdateDTO.getRaffleStartAt(), goodsUpdateDTO.getRaffleEndAt());
        goods.updateGoods(goodsUpdateDTO,status);

        Categories category = categoriesQueryService.getCategoriesByName(goodsUpdateDTO.getCategory());
        GoodsCategories goodsCategories = goodsCategoriesQueryService.getGoodsCategoriesByGoodsId(goods);
        goodsCategories.updateGoodsCategories(goods, category);

        Goods updatedGoods = goodsQueryService.getGoodsById(goodsId);
        return GoodsResponse.GoodsAddResponseDTO.of(updatedGoods, category.getName());
    }

    // 상품 삭제
    @Override
    public GoodsResponse.GoodsDeleteResponseDTO deleteGoods(List<Long> deleteList, Members member) {
        if (member.getRole() == Role.MEMBER) {
            throw new ApiException(ErrorStatus._ACCESS_DENIED_FOR_MEMBER);
        }

        List<Long> deletedList = new ArrayList<>();

        for(Long id:deleteList){
            Goods goods = goodsQueryService.getGoodsById(id);
            goodsRepository.delete(goods);
            deletedList.add(id);
        }
        return GoodsResponse.GoodsDeleteResponseDTO.from(deletedList);
    }

    // 상품 상세 조회
    @Override
    public GoodsResponse.GoodsDetailResponseDTO getGoodsInfo(Long goodsId) {
        Goods goods = goodsQueryService.getGoodsById(goodsId);
        GoodsCategories goodsCategories = goodsCategoriesQueryService.getGoodsCategoriesByGoodsId(goods);
        Categories category = categoriesQueryService.getCategoriesById(goodsCategories.getCategoryId().getId());
        return GoodsResponse.GoodsDetailResponseDTO.of(goods, category.getName());
    }

    // 상품 검색
    @Override
    public GoodsResponse.GoodsListSearchResponseDTO searchGoods(GoodsStatus goodsStatus, Integer page, Integer size, String keyword, GoodsFilter sortBy, String category, Members member) {
        PageRequest pageRequest = goodsQueryService.createPageRequest(page, size, sortBy); // 종료임박순, 최신순 처리
        // 카테고리 ID를 조회
        Long categoryId = null;
        if (category != null && !category.isEmpty()) {
            Categories categories = categoriesQueryService.getCategoriesByName(category);
            categoryId = categories.getId();
        }
        // 상품 검색 쿼리 호출
        Page<Goods> goodsPage;
        if (GoodsFilter.POPULAR.equals(sortBy)) { // 인기순 처리
            goodsPage = goodsRepository.searchGoodsByRaffleCount(goodsStatus, categoryId, keyword, pageRequest);
        } else {
            goodsPage = goodsRepository.searchGoods(goodsStatus, categoryId, keyword, pageRequest);
        }
        int total=(int)goodsPage.getTotalElements();

        List<GoodsResponse.GoodsSearchResponseDTO> goodsSearchDTOList = goodsPage.getContent().stream()
                .map(g -> GoodsResponse.GoodsSearchResponseDTO.of(g, category, member))
                .toList();
        return GoodsResponse.GoodsListSearchResponseDTO.builder()
                .goodsSearchDTOList(goodsSearchDTOList)
                .totalCnt(total)
                .build();
    }

}

