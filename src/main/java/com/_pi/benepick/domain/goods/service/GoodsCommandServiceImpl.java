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
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.domain.wishlists.entity.Wishlists;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
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
    public GoodsResponse.GoodsAddResponseDTO updateGoods(Long goodsId, GoodsRequest.GoodsRequestDTO goodsUpdateDTO) {
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

    // 상품 삭제
    @Override
    public GoodsResponse.GoodsDeleteResponseDTO deleteGoods(List<Long> deleteList) {
        List<Goods> goodsList = goodsRepository.findAllById(deleteList);
        if (goodsList.size() != deleteList.size()) {
            throw new ApiException(ErrorStatus._GOODS_NOT_FOUND);
        }
        for (Goods goods : goodsList) {
            goods.updateDeleted();
            for (Raffles raffle : goods.getRaffles()) {
                raffle.updateDeleted();
                if (raffle.getDraw() != null) {
                    raffle.getDraw().updateDeleted();
                }
            }
            for (Wishlists wishlist : goods.getWishlists()) {
                wishlist.updateDeleted();
            }
            if (goods.getGoodsCategories() != null) {
                goods.getGoodsCategories().updateDeleted();
            }
            if (goods.getHash() != null) {
                goods.getHash().updateDeleted();
            }
            goodsRepository.save(goods);
        }
        return GoodsResponse.GoodsDeleteResponseDTO.from(goodsList);
    }
}