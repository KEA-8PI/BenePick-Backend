package com._pi.benepick.domain.goodsCategories.entity;

import com._pi.benepick.global.common.BaseJPATimeEntity;
import com._pi.benepick.domain.categories.entity.Categories;
import com._pi.benepick.domain.goods.entity.Goods;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@SQLRestriction("is_deleted = 'F'")
@SQLDelete(sql = "UPDATE goods_categories SET is_deleted = 'T' WHERE id = ?")
public class GoodsCategories extends BaseJPATimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //상품카테고리_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Categories categoryId; //카테고리_id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id")
    private Goods goodsId; //상품_id

    public void updateGoodsCategories(Goods goods, Categories categories) {
        this.goodsId = goods;
        this.categoryId = categories;
    }
}
