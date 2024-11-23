package com.pwr.gastromate.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CategoryRevenueDTO {
    private String categoryName;
    private BigDecimal totalRevenue;
    private Long quantity;

    public CategoryRevenueDTO(String categoryName, Long totalRevenue, Long quantity) {
        this.categoryName = categoryName;
        this.totalRevenue = BigDecimal.valueOf(totalRevenue);
        this.quantity = quantity;
    }

    public CategoryRevenueDTO(String categoryName, BigDecimal totalRevenue, Long quantity) {
        this.categoryName = categoryName;
        this.totalRevenue = totalRevenue;
        this.quantity = quantity;

    }
}
