package com.pwr.gastromate.dto.restock;

import com.pwr.gastromate.dto.ingredient.IngredientDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MissingIngredientDTO {
    private IngredientDTO ingredient;
    private BigDecimal quantitySum;
    private Double averageUsage;
    private BigDecimal difference;
    private StockStatus status;

}


