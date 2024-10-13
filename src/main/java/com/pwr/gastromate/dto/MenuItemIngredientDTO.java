package com.pwr.gastromate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemIngredientDTO {
    private Integer menuItemId;
    private Integer ingredientId;
    private String ingredientName;  // Dodajemy nazwę składnika
    private Float quantityRequired;
}
