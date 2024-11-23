package com.pwr.gastromate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.text.StyledEditorKit;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MissingIngredientDTO {
    private IngredientDTO ingredient;
    private Double averageUsage;
    private Double difference;
    private StockStatus status;

}


