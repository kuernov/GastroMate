package com.pwr.gastromate.service.mapper;

import com.pwr.gastromate.data.Category;
import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.CategoryDTO;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public static CategoryDTO toDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName());
    }

    public static Category toEntity(CategoryDTO categoryDTO) {
        return new Category(categoryDTO.getName());
    }
}
