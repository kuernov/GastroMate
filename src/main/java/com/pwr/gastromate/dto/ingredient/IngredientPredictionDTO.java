package com.pwr.gastromate.dto.ingredient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientPredictionDTO {
    private String ingredientName;
    private List<String> date;
    private List<BigDecimal> predictedValue;
    private BigDecimal totalPredictedValue;

    public IngredientPredictionDTO(String ingredientName, List<String> date, List<BigDecimal> predictedValue) {
        this.ingredientName = ingredientName;
        this.date = date;
        this.predictedValue = predictedValue;
        this.totalPredictedValue = predictedValue.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Obliczanie sumy
    }
}
