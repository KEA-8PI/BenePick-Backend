package com._pi.benepick.domain.goodsCategories.entity;

import com._pi.benepick.config.BaseJPATimeEntity;
import com._pi.benepick.domain.categories.entity.Categories;
import com._pi.benepick.domain.goods.entity.Goods;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("is_deleted = 'F'")
@SQLDelete(sql = "UPDATE goods_categories SET is_deleted = 'T' WHERE goods_id = ?")
public class GoodsCategories extends BaseJPATimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //상품카테고리_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Categories categoryId; //카테고리_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id")
    private Goods goodsId; //상품_id
}
