package com._pi.benepick.domain.goods.entity;

import com._pi.benepick.domain.goods.dto.GoodsRequest;
import com._pi.benepick.domain.goodsCategories.entity.GoodsCategories;
import com._pi.benepick.domain.hash.entity.Hash;
import com._pi.benepick.domain.raffles.entity.Raffles;
import com._pi.benepick.domain.wishlists.entity.Wishlists;
import com._pi.benepick.global.common.BaseJPATimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
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
    @Lob
    @Column(columnDefinition = "TEXT")
    private String image; //상품 사진
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description; //설명

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Hash.class)
    @JoinColumn(name = "hash_id")
    private Hash hash; //해시값

    @Enumerated(EnumType.STRING)
    private GoodsStatus goodsStatus; //상품응모상태 (PROGRESS,SCHEDULED,COMPLETED)

    //
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "goodsId", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Raffles> raffles; // 응모자 리스트

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "goodsId", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Wishlists> wishlists; // 위시리스트

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "goodsId", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private GoodsCategories goodsCategories; // 상품 카테고리

    public void startDraw(Hash hash, GoodsStatus goodsStatus) {
        this.hash = hash;
        this.goodsStatus = goodsStatus;
    }

    public void updateStatus(GoodsStatus newStatus) {
        this.goodsStatus = newStatus;
    }

    public boolean isWishlistForMember(String memberId) {
        return wishlists.stream()
                .anyMatch(wishlist -> wishlist.getMemberId().getId().equals(memberId));
    }

    public void updateGoods(GoodsRequest.GoodsRequestDTO goodsUpdateDTO, GoodsStatus goodsStatus){
        this.name = Objects.nonNull( goodsUpdateDTO.getName())? goodsUpdateDTO.getName() : this.name;
        this.amounts = Objects.nonNull( goodsUpdateDTO.getAmounts())? goodsUpdateDTO.getAmounts() : this.amounts;
        this.image = Objects.nonNull( goodsUpdateDTO.getImage())? goodsUpdateDTO.getImage() : this.image;
        this.description = Objects.nonNull( goodsUpdateDTO.getDescription())? goodsUpdateDTO.getDescription() : this.description;
        this.price = Objects.nonNull( goodsUpdateDTO.getPrice())? goodsUpdateDTO.getPrice() : this.price;
        this.discountPrice = Objects.nonNull( goodsUpdateDTO.getDiscountPrice())? goodsUpdateDTO.getDiscountPrice() : this.discountPrice;
        this.raffleStartAt = Objects.nonNull( goodsUpdateDTO.getRaffleStartAt())? goodsUpdateDTO.getRaffleStartAt() : this.raffleStartAt;
        this.raffleEndAt = Objects.nonNull( goodsUpdateDTO.getRaffleEndAt())? goodsUpdateDTO.getRaffleEndAt() : this.raffleEndAt;
        this.goodsStatus = goodsStatus;
    }
}
