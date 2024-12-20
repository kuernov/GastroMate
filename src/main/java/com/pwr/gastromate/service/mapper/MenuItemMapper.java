package com.pwr.gastromate.service.mapper;

import com.pwr.gastromate.data.menuItem.MenuItem;
import com.pwr.gastromate.dto.menu.MenuItemDTO;
import com.pwr.gastromate.dto.menu.MenuItemIngredientDTO;

import com.pwr.gastromate.data.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MenuItemMapper {

    public static MenuItemDTO toDTO(MenuItem menuItem) {
        MenuItemDTO dto = new MenuItemDTO();
        dto.setId(menuItem.getId());
        dto.setName(menuItem.getName());
        dto.setDescription(menuItem.getDescription());
        dto.setPrice(menuItem.getPrice());
        dto.setCategoryIds(menuItem.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));

        // Mapowanie składników z użyciem posortowanej listy
        List<MenuItemIngredientDTO> ingredientDTOs = menuItem.getSortedMenuItemIngredients().stream()
                .map(ingredient -> new MenuItemIngredientDTO(
                        ingredient.getIngredient().getId(),
                        ingredient.getMenuItem().getId(),
                        ingredient.getIngredient().getName(),
                        ingredient.getQuantityRequired(),
                        ingredient.getUnit().getId()
                ))
                .collect(Collectors.toList());

        dto.setIngredients(ingredientDTOs);
        return dto;
    }

    public static MenuItem toEntity(MenuItemDTO menuItemDTO, Set<Category> categories) {
        MenuItem menuItem = new MenuItem();
        menuItem.setName(menuItemDTO.getName());
        menuItem.setDescription(menuItemDTO.getDescription());
        menuItem.setPrice(menuItemDTO.getPrice());
        menuItem.setCategories(categories);

        return menuItem;
    }
}


