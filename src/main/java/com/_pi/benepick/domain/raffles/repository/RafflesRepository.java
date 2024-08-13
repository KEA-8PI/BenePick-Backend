package com._pi.benepick.domain.raffles.repository;

import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.goods.entity.GoodsStatus;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.raffles.entity.Raffles;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RafflesRepository extends JpaRepository<Raffles, Long> {

    List<Raffles> findAllByGoodsIdOrderByPointAsc(Goods goods);

    List<Raffles> findAllByGoodsIdOrderByPointDesc(Goods goods);

    List<Raffles> findAllByGoodsId(Goods goods);

    List<Raffles> findAllByMemberId(Members members);

    Optional<Raffles> findByGoodsIdAndMemberId(Goods goods, Members members);

    void deleteAllByMemberId_IdAndGoodsId_GoodsStatus(String id,GoodsStatus goodsStatus);

}