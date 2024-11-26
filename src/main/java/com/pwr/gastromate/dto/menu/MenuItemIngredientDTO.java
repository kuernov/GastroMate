package com.pwr.gastromate.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemIngredientDTO {
    private Integer menuItemId;
    private Integer ingredientId;
    private String ingredientName;  // Dodajemy nazwę składnika
    private BigDecimal quantityRequired;
    private Integer unitId;
}
