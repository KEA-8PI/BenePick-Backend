package com._pi.benepick.domain.wishlists.repository;

import com._pi.benepick.domain.wishlists.entity.Wishlists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface WishlistsRepository extends JpaRepository<Wishlists, Long> {

void deleteAllByMemberId_Id(String id);
    void deleteAllByGoodsId_Id(Long id);
}
