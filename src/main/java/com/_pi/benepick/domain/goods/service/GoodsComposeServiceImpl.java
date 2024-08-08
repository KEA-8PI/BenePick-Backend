package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.categories.entity.Categories;
import com._pi.benepick.domain.categories.repository.CategoriesRepository;
import com._pi.benepick.domain.draws.repository.DrawsRepository;
import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goods.repository.GoodsRepository;
import com._pi.benepick.domain.goodsCategories.entity.GoodsCategories;
import com._pi.benepick.domain.goodsCategories.repository.GoodsCategoriesRepository;
import com._pi.benepick.domain.hash.repository.HashsRepository;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.domain.raffles.repository.RafflesRepository;
import com._pi.benepick.domain.wishlists.repository.WishlistsRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
    private final GoodsCommandServiceImpl goodsCommandService;
    private final MembersRepository membersRepository;
    private final RafflesRepository rafflesRepository;
    private final DrawsRepository drawsRepository;
    private final WishlistsRepository wishlistsRepository;
    private final HashsRepository hashsRepository;

    // 상품 파일 추가
    @Override
    public GoodsResponse.GoodsUploadResponseDTO uploadGoodsFile(MultipartFile file) {
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
                GoodsStatus status = goodsCommandService.determineGoodsStatus(raffleStartAt, raffleEndAt);
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

    // 상품 삭제
    @Override
    public GoodsResponse.GoodsDeleteResponseDTO deleteGoods(List<Long> deleteList, Members members) {
        //관리자 인지 확인하는 로직
        if(membersRepository.findById(members.getId()).get().getRole()== Role.MEMBER){
            throw new ApiException(ErrorStatus._UNAUTHORIZED);
        }
        List<Long> deletedList = new ArrayList<>();

        for(Long id:deleteList){
            Goods goods = goodsRepository.findById(id).orElseThrow(()->new ApiException(ErrorStatus._GOODS_NOT_FOUND));
            for(Raffles raffle : goods.getRaffles()){
                drawsRepository.deleteAllByRaffleId_Id(raffle.getId());
            }
            rafflesRepository.deleteAllByGoodsId_Id(id);
            wishlistsRepository.deleteAllByGoodsId_Id(id);
            goodsCategoriesRepository.deleteAllByGoodsId_Id(id);
            hashsRepository.deleteById(goods.getHash().getId());
            goodsRepository.deleteById(id);
            deletedList.add(id);
        }
        return GoodsResponse.GoodsDeleteResponseDTO.from(deletedList);
    }
}

