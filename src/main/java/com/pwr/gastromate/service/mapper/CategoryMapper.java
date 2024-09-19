package com.pwr.gastromate.service.mapper;

import com.pwr.gastromate.data.Category;
import com.pwr.gastromate.dto.CategoryDTO;

public class CategoryMapper {

    public static CategoryDTO toDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName(), category.getUser());
    }

    public static Category toEntity(CategoryDTO categoryDTO) {
        return new Category(categoryDTO.getName(), categoryDTO.getUser());
    }
}
