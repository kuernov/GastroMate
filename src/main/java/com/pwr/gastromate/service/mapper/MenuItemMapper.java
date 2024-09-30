package com.pwr.gastromate.service.mapper;

import com.pwr.gastromate.data.MenuItem;
import com.pwr.gastromate.dto.MenuItemDTO;
import com.pwr.gastromate.data.Category;
import com.pwr.gastromate.data.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MenuItemMapper {

    // Mapowanie MenuItem na MenuItemDTO
    public static MenuItemDTO toDTO(MenuItem menuItem) {
        MenuItemDTO dto = new MenuItemDTO();
        dto.setId(menuItem.getId());
        dto.setName(menuItem.getName());
        dto.setDescription(menuItem.getDescription());
        dto.setPrice(menuItem.getPrice());
        dto.setCategoryIds(menuItem.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
        dto.setUserId(menuItem.getUser().getId());
        return dto;
    }

    // Mapowanie MenuItemDTO na MenuItem
    public static MenuItem toEntity(MenuItemDTO dto, Set<Category> categories, User user) {
        MenuItem menuItem = new MenuItem();
        menuItem.setId(dto.getId());
        menuItem.setName(dto.getName());
        menuItem.setDescription(dto.getDescription());
        menuItem.setPrice(dto.getPrice());
        menuItem.setCategories(categories);  // Używamy pełnych encji Category
        menuItem.setUser(user);  // Używamy pełnej encji User
        return menuItem;
    }
}

