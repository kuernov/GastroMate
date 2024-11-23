package com.pwr.gastromate.dto;

import com.pwr.gastromate.data.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
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
