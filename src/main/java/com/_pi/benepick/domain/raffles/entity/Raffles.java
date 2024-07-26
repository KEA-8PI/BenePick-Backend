package com._pi.benepick.domain.raffles.entity;

import com._pi.benepick.global.common.BaseJPATimeEntity;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.members.entity.Members;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("is_deleted = 'F'")
@SQLDelete(sql = "UPDATE raffles SET is_deleted = 'T' WHERE id = ?")
public class Raffles extends BaseJPATimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //응모_id

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Members.class)
    @JoinColumn(name = "member_id")
    private Members memberId; //멤버_id

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Goods.class)
    @JoinColumn(name = "goods_id")
    private Goods goodsId; //상품_id

    private Long point; //사용포인트

}
