package com.pwr.gastromate.dto.menu;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuItemRequest {
    private MenuItemDTO menuItem;
    private List<MenuItemIngredientDTO> menuItemIngredients;

    // Getters i setters
}
