package com._pi.benepick.domain.wishlists.repository;

import com._pi.benepick.domain.wishlists.entity.Wishlists;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistsRepository extends JpaRepository<Wishlists, Long> {
    void deleteAllByMemberId_Id(String id);
}
