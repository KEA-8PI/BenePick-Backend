package com._pi.benepick.domain.raffles.entity;

import com._pi.benepick.domain.draws.entity.Draws;
import com._pi.benepick.global.common.BaseJPATimeEntity;
import com._pi.benepick.domain.goods.entity.Goods;
import com._pi.benepick.domain.members.entity.Members;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    @Column(nullable = false)
    private Long point; //사용포인트

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "raffleId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Draws draw; // 추첨
    @Column(nullable = false)
    @Builder.Default
    private char penaltyFlag = 'F';

    public void updatePenaltyFlag(char penaltyFlag) {
        this.penaltyFlag = penaltyFlag;
    }

    public void increasePoint(Long point) {
        this.point = this.point + point;
    }

}