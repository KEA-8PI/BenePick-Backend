package com._pi.benepick.domain.wishlists.controller;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.repository.GoodsRepository;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.domain.wishlists.entity.Wishlists;
import com._pi.benepick.domain.wishlists.repository.WishlistsRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Wishlists", description = "위시리스트 API")
@RequestMapping("/wishlists")
public class WishlistsController {
    private final WishlistsRepository wishlistsRepository;
    private final MembersRepository membersRepository;
    private final GoodsRepository goodsRepository;

    @PostMapping("/add")
    public void addWishlists() {
        Goods goods = goodsRepository.findById(2L).get();
        Members member = membersRepository.findById("02c1a091-0707-42d6-a822-018d8ea7e702").get();

        Wishlists wishlists = Wishlists.builder()
                .goodsId(goods)
                .memberId(member)
                .build();

        wishlistsRepository.save(wishlists);
    }
}
