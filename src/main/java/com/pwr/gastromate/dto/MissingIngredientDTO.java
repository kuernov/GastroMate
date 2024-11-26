package com.pwr.gastromate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.text.StyledEditorKit;
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


