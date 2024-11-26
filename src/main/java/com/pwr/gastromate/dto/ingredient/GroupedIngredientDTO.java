package com.pwr.gastromate.dto.ingredient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupedIngredientDTO {
    private String name;
    private double totalQuantity;
    private List<IngredientDTO> ingredientDTOList;
}
