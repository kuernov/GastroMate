package com.pwr.gastromate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientPredictionDTO {
    private String ingredientName;
    private List<String> date;
    private List<Double> predictedValue;
}
