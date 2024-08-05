package com._pi.benepick.domain.raffles.repository;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.raffles.entity.Raffles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RafflesRepository extends JpaRepository<Raffles, Long> {

    List<Raffles> findAllByGoodsId(Goods goods);

    List<Raffles> findAllByMemberId(Members members);

    @Query("SELECT r.id FROM Raffles r WHERE r.goodsId.id = :goodsId")
    List<Long> findRaffleIdsByGoodsId(Long goodsId);
    Optional<Raffles> findByGoodsIdAndMemberId(Goods goods, Members members);
}