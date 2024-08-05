package com._pi.benepick.domain.wishlists.repository;

import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.wishlists.entity.Wishlists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WishlistsRepository extends JpaRepository<Wishlists, Long> {

    @Query("select w FROM Wishlists w left join w.goodsId.raffles r "
    +"order by count(r) desc, w.goodsId.id asc")
    Page<Wishlists> searchWishlistsByRaffleCount(GoodsStatus goodsStatus, Pageable pageable);
}
