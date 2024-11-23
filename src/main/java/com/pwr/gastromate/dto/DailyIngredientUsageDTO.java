package com.pwr.gastromate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyIngredientUsageDTO {
    private LocalDate orderDate;       // Zmiana na LocalDate
    private Integer ingredientId;
    private String ingredientName;
    private BigDecimal dailyConsumption;

    // Dodatkowy konstruktor dla podstawowych danych
    public DailyIngredientUsageDTO(LocalDate orderDate, BigDecimal dailyConsumption) {
        this.orderDate = orderDate;
        this.dailyConsumption = dailyConsumption;
    }
}
