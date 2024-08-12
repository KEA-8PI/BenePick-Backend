package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.categories.entity.Categories;
import com._pi.benepick.domain.goods.entity.GoodsFilter;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goodsCategories.entity.GoodsCategories;
import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.categories.repository.CategoriesRepository;
import com._pi.benepick.domain.goodsCategories.repository.GoodsCategoriesRepository;
import com._pi.benepick.domain.goods.repository.GoodsRepository;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoodsQueryServiceImpl implements GoodsQueryService {

    private final GoodsRepository goodsRepository;
    private final GoodsCategoriesRepository goodsCategoriesRepository;
    private final CategoriesRepository categoriesRepository;

    // 상품 목록 조회 (+ 검색)
    @Override
    public GoodsResponse.GoodsListResponseDTO getGoodsList(Integer page, Integer size, String keyword, Members member) {
        if (member.getRole() == Role.MEMBER) {
            throw new ApiException(ErrorStatus._ACCESS_DENIED_FOR_MEMBER);
        }
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Goods> goodsPage;
        if (keyword != null && !keyword.isEmpty()) {
            goodsPage = goodsRepository.findByNameContainingIgnoreCase(keyword, pageRequest);
        } else {
            goodsPage = goodsRepository.findAll(pageRequest);
        }
        int total=(int)goodsPage.getTotalElements();
        List<GoodsResponse.GoodsResponseDTO> goodsDTOList = goodsPage.getContent().stream()
                .map(GoodsResponse.GoodsResponseDTO::from)
                .toList();

        return GoodsResponse.GoodsListResponseDTO.builder()
                .goodsDTOList(goodsDTOList)
                .totalCnt(total)
                .build();
    }

    // 상품 상세 조회
    @Override
    public GoodsResponse.GoodsDetailResponseDTO getGoodsInfo(Long goodsId) {
        Goods goods = goodsRepository.findById(goodsId).orElseThrow(() -> new ApiException(ErrorStatus._GOODS_NOT_FOUND));
        GoodsCategories goodsCategories = goodsCategoriesRepository.findByGoodsId(goods).orElseThrow(() -> new ApiException(ErrorStatus._GOODS_CATEGORY_NOT_FOUND));
        Categories category = categoriesRepository.findById(goodsCategories.getCategoryId().getId()).orElseThrow(() -> new ApiException(ErrorStatus._CATEGORY_NOT_FOUND));
        return GoodsResponse.GoodsDetailResponseDTO.of(goods, category.getName());
    }

    // 시드 값 조회
    @Override
    public GoodsResponse.GoodsSeedsResponseDTO getSeeds(Long goodsId) {
        Goods goods = goodsRepository.findById(goodsId).orElseThrow(() -> new ApiException(ErrorStatus._GOODS_NOT_FOUND));
        return GoodsResponse.GoodsSeedsResponseDTO.from(goods);
    }

    // 상품 검색
    @Override
    public GoodsResponse.GoodsListSearchResponseDTO searchGoods(GoodsStatus goodsStatus, Integer page, Integer size, String keyword, GoodsFilter sortBy, String category, Members member) {
        PageRequest pageRequest = createPageRequest(page, size, sortBy); // 종료임박순, 최신순 처리
        // 카테고리 ID를 조회
        Long categoryId = null;
        if (category != null && !category.isEmpty()) {
            Optional<Categories> categoryEntityOptional = categoriesRepository.findByName(category);
            if (categoryEntityOptional.isPresent()) {
                categoryId = categoryEntityOptional.get().getId();
            }
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

    // 선택된 기준으로 정렬
    private PageRequest createPageRequest(Integer page, Integer size, GoodsFilter sortBy) {
        Sort sort;
        switch (sortBy) {
            case NEWEST: // 최신순
                sort = Sort.by(Sort.Order.desc("createdAt"), Sort.Order.asc("id"));
                break;
            case END: // 종료임박순
                sort = Sort.by(Sort.Order.asc("raffleEndAt"), Sort.Order.asc("id"));
                break;
            default:
                sort = Sort.by(Sort.Order.desc("id")); // 기본
        }
        return PageRequest.of(page, size, sort);
    }

    public Goods goodsFindById(Long goodsId) {
        return goodsRepository.findById(goodsId).orElseThrow(() -> new ApiException(ErrorStatus._GOODS_NOT_FOUND));
    }

    public List<Goods> findByRaffleEndAtBeforeAndGoodsStatus(LocalDateTime now) {
        return goodsRepository.findByRaffleEndAtBeforeAndGoodsStatus(now, GoodsStatus.PROGRESS);
    }
}