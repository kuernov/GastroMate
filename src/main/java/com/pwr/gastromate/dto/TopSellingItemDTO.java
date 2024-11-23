package com.pwr.gastromate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class TopSellingItemDTO {
    private String itemName;
    private Long quantitySold;
    private BigDecimal totalRevenue;
    private double quantityPercentage; // Nowe pole na procentowy udział ilości
    private double revenuePercentage;

    public TopSellingItemDTO(String name, Long quantitySold, Long totalRevenue){
        this.itemName = name;
        this.quantitySold = quantitySold;
        this.totalRevenue = BigDecimal.valueOf(totalRevenue);
    }

    public TopSellingItemDTO(String name, Long quantitySold, BigDecimal totalRevenue){
        this.itemName = name;
        this.quantitySold = quantitySold;
        this.totalRevenue = totalRevenue;
    }
}
