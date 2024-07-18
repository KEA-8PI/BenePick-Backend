package com._pi.benepick.domain.goods.entity;

import com._pi.benepick.config.BaseJPATimeEntity;
import com._pi.benepick.domain.Status;
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
public class Goods extends BaseJPATimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //상품_id
    private String name; //이름
    private Long amounts; //수량
    private String description; //설명
    private String category; //카테고리
    private String itemStatus; //아이템상태
    private String seeds; //시드값
    private Long price; //정가
    private Long discount; //할인율
    private String goodsImage; //상품 사진
    private String goodsThumb; //상품 썸네일
    private LocalDateTime raffleStartAt; //응모 시작일
    private LocalDateTime raffleEndAt; //응모 종료일
    private Status status; //상태
}
