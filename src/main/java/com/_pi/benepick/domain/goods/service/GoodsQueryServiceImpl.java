package com._pi.benepick.domain.goods.service;

import com._pi.benepick.domain.categories.entity.Categories;
import com._pi.benepick.domain.goods.entity.GoodsFilter;
import com._pi.benepick.domain.goods.dto.GoodsResponse;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goods.repository.GoodsRepository;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoodsQueryServiceImpl implements GoodsQueryService {

    private final GoodsRepository goodsRepository;

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

    // 시드 값 조회
    @Override
    public GoodsResponse.GoodsSeedsResponseDTO getSeeds(Long goodsId) {
        Goods goods = getGoodsById(goodsId);
        return GoodsResponse.GoodsSeedsResponseDTO.from(goods);
    }

    // 선택된 기준으로 정렬
    @Override
    public PageRequest createPageRequest(Integer page, Integer size, GoodsFilter sortBy) {
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

    @Override
    public Goods getGoodsById(Long id){
        return goodsRepository.findById(id).orElseThrow(()->new ApiException(ErrorStatus._GOODS_NOT_FOUND));
    }

    @Override
    public List<Goods> findByRaffleEndAtBeforeAndGoodsStatus(LocalDateTime now) {
        return goodsRepository.findByRaffleEndAtBeforeAndGoodsStatus(now, GoodsStatus.PROGRESS);
    }

    @Override
    public List<Goods> getAll() {
        return goodsRepository.findAll();
    }

    @Override
    public List<Goods> getGoodsByCategoryIdAndDateRange(Categories category, LocalDateTime startDate, LocalDateTime endDate) {
        return goodsRepository.getGoodsByCategoryIdAndDateRange(category, startDate, endDate);
    }
}