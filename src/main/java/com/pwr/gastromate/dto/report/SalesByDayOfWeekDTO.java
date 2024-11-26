package com.pwr.gastromate.dto.report;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class SalesByDayOfWeekDTO {
    private String dayOfWeek;
    private Long totalQuantity;
    private BigDecimal totalRevenue;

    public SalesByDayOfWeekDTO(Integer dayOfWeek, Long totalQuantity, BigDecimal totalRevenue){
        this.dayOfWeek =mapDayOfWeek(dayOfWeek);
        this.totalQuantity = totalQuantity;
        this.totalRevenue = totalRevenue;
    }

    public SalesByDayOfWeekDTO(Integer dayOfWeek, Long totalQuantity, Long totalRevenue){
        this.dayOfWeek =mapDayOfWeek(dayOfWeek);
        this.totalQuantity = totalQuantity;
        this.totalRevenue = BigDecimal.valueOf(totalRevenue);
    }

    private String mapDayOfWeek(Integer dayOfWeekNumber) {
        return switch (dayOfWeekNumber) {
            case 7 -> "Sunday";
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            case 6 -> "Saturday";
            default -> "Unknown";
        };
    }
}
