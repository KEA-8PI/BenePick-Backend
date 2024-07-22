package com._pi.benepick.domain.goods.repository;

import com._pi.benepick.domain.goods.entity.Categories;
import com._pi.benepick.domain.goods.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {
}
