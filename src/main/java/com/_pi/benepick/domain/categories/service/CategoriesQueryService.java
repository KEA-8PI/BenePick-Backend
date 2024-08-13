package com._pi.benepick.domain.categories.service;

import com._pi.benepick.domain.categories.entity.Categories;

public interface CategoriesQueryService {

    Categories getCategoriesByName(String categoryName);
}
