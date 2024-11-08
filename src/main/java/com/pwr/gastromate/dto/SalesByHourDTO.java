package com.pwr.gastromate.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SalesByHourDTO {
    private String hour;
    private Long totalQuantity;
    private BigDecimal totalRevenue;

    public SalesByHourDTO(String hour, Long totalQuantity, BigDecimal totalRevenue){
        this.hour = hour;
        this.totalQuantity = totalQuantity;
        this.totalRevenue = totalRevenue;
    }

    public SalesByHourDTO(String dayOfWeek, Long totalQuantity, Long totalRevenue){
        this.hour =dayOfWeek;
        this.totalQuantity = totalQuantity;
        this.totalRevenue = BigDecimal.valueOf(totalRevenue);
    }
}
