package com._pi.benepick.domain.goods.entity;

import com._pi.benepick.global.common.BaseJPATimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@SQLRestriction("is_deleted = 'F'")
@SQLDelete(sql = "UPDATE goods SET is_deleted = 'T' WHERE id = ?")
public class Goods extends BaseJPATimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //상품_id

    @Column(nullable = false)
    private String name; //이름
    @Column(nullable = false)
    private Long amounts; //수량
    @Column(nullable = false)
    private Long price; //정가
    @Column(nullable = false)
    private Long discountPrice; //할인가
    @Column(nullable = false)
    private LocalDateTime raffleStartAt; //응모 시작일
    @Column(nullable = false)
    private LocalDateTime raffleEndAt; //응모 종료일

    private String image; //상품 사진
    private String description; //설명
    @Builder.Default
    private Long seeds = -1L; //시드값

    @Enumerated(EnumType.STRING)
    private GoodsStatus goodsStatus; //상품응모상태 (PROGRESS,SCHEDULED,COMPLETED)
}
