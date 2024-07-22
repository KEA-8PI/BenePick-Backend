package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.goods.entity.Categories;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.repository.GoodsRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import jdk.jfr.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsQueryServiceImpl implements GoodsQueryService{

    private final GoodsRepository goodsRepository;


    // 상품 상세 조회
    @Override
    public GoodsResponse.GoodsResponseDTO getGoodsInfo(Long goodsId) {
        Goods goods = goodsRepository.findById(goodsId).orElseThrow(() -> new ApiException(ErrorStatus._GOODS_NOT_FOUND));
        Categories category =
        return GoodsResponse.GoodsResponseDTO.builder()
                .id(goods.getId())
                .name(goods.getName())
                .amounts(goods.getAmounts())
                .image(goods.getImage())
                .description(goods.getDescription())
                .goodsStatus(goods.getGoodsStatus())
                .price(goods.getPrice())
                .discountPrice(goods.getDiscountPrice())
                .raffleStartAt(goods.getRaffleStartAt())
                .raffleEndAt(goods.getRaffleEndAt())
                .build();
    }

    // 상품 목록 조회
    @Override
    public List<GoodsResponse.GoodsListResponseDTO> getGoodsList() {
        List<Goods> goods = goodsRepository.findByUser_id(user_id);
        return goods.stream()
                .map(folder -> GoodsResponse.GoodsListResponseDTO.builder()
                        .id(folder.getId())
                        .folderName(folder.getFolderName())
                        .build())
                .collect(Collectors.toList());
    }

}
