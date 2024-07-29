package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.dto.DrawsResponse;
import com._pi.benepick.domain.draws.repository.DrawsRepository;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.goodsCategories.repository.GoodsCategoriesRepository;
import com._pi.benepick.domain.draws.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DrawsQueryServiceImpl implements DrawsQueryService {

    private final DrawsRepository drawsRepository;
    private final GoodsCategoriesRepository goodsCategoriesRepository;

    public DrawsResponse.DrawsResponseByGoodsListDTO getWaitlistByGoodsId(Long goodsId) {
        List<DrawsResponse.DrawsResponseByGoodsDTO> drawsResponseByGoodsDTOS = (drawsRepository.findByGoodsId(goodsId)).stream()
                .filter(draws -> draws.getStatus() == Status.WAITLIST)
                .map(DrawsResponse.DrawsResponseByGoodsDTO::from)
                .collect(Collectors.toList());

        return DrawsResponse.DrawsResponseByGoodsListDTO.builder()
                .drawsResponseByGoodsDTOList(drawsResponseByGoodsDTOS)
                .build();
    }

    public DrawsResponse.DrawsResponseByGoodsListDTO getWinnersByGoodsId(Long goodsId) {
        List<DrawsResponse.DrawsResponseByGoodsDTO> drawsResponseByGoodsDTOS = (drawsRepository.findByGoodsId(goodsId)).stream()
                .filter(draws -> draws.getStatus() != Status.WAITLIST && draws.getStatus() != Status.NON_WINNER)
                .map(DrawsResponse.DrawsResponseByGoodsDTO::from)
                .collect(Collectors.toList());

        return DrawsResponse.DrawsResponseByGoodsListDTO.builder()
                .drawsResponseByGoodsDTOList(drawsResponseByGoodsDTOS)
                .build();
    }

    public DrawsResponse.DrawsResponseByMembersListDTO getCompleteRafflesByMemberId(String memberId) {
        List<DrawsResponse.DrawsResponseByMembersDTO> drawsResponseByMembersDTOS = (drawsRepository.findByMemberId(memberId)).stream()
                .filter(draws -> draws.getRaffleId().getGoodsId().getGoodsStatus() == GoodsStatus.COMPLETED)
                .map(draws -> {
                    String categoryName = (goodsCategoriesRepository.findByGoodsId(draws.getRaffleId().getGoodsId())).map(goodsCategories -> goodsCategories.getCategoryId().getName()).orElse("NONE");

                    return DrawsResponse.DrawsResponseByMembersDTO.of(draws, categoryName);
                })
                .collect(Collectors.toList());

        return DrawsResponse.DrawsResponseByMembersListDTO.builder()
                .drawsResponseByMembersList(drawsResponseByMembersDTOS)
                .build();
    }


}
