package com._pi.benepick.domain.raffles.entity;

import com._pi.benepick.config.BaseJPATimeEntity;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.members.entity.Members;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Raffles extends BaseJPATimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //응모_id

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Members.class)
    @JoinColumn(name = "member_id")
    private String memberId; //멤버_id

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Goods.class)
    @JoinColumn(name = "goods_id")
    private Long goodsId; //상품_id

    private Long point; //사용포인트

}
