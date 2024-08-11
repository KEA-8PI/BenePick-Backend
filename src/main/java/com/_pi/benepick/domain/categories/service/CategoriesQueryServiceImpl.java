package com._pi.benepick.domain.categories.service;

import com._pi.benepick.domain.categories.entity.Categories;
import com._pi.benepick.domain.categories.repository.CategoriesRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoriesQueryServiceImpl implements CategoriesQueryService {
    private final CategoriesRepository categoriesRepository;
    @Override
    public Categories getCategoriesByName(String categoryName) {
        return categoriesRepository.findByName(categoryName).orElseThrow(() -> new ApiException(
            ErrorStatus._CATEGORY_NOT_FOUND));
    }
}
