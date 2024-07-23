package com._pi.benepick.domain.goods.entity;

import com._pi.benepick.config.BaseJPATimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("is_deleted = 'F'")
@SQLDelete(sql = "UPDATE goods SET is_deleted = 'T' WHERE id = ?")
public class Goods extends BaseJPATimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //상품_id
    private String name; //이름
    private Long amounts; //수량
    private String image; //상품 사진
    private String description; //설명

    @Enumerated(EnumType.STRING)
    private GoodsStatus goodsStatus; //상품응모상태 (PROGRESS,SCHEDULED,COMPLETED)

    @Column(nullable = true)
    private String seeds; //시드값

    private Long price; //정가
    private Long discountPrice; //할인가
    private LocalDateTime raffleStartAt; //응모 시작일
    private LocalDateTime raffleEndAt; //응모 종료일
}
