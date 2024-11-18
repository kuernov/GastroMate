package com.pwr.gastromate.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;


@Getter
@Setter
public class DailyIngredientUsageDTO {
    private Date orderDate;
    private Integer ingredientId;
    private String ingredientName;
    private BigDecimal dailyConsumption;

    public DailyIngredientUsageDTO(Date orderDate, Integer ingredientId, String ingredientName, BigDecimal dailyConsumption) {
        this.orderDate = orderDate;
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.dailyConsumption = dailyConsumption;
    }
}
